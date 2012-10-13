local require = require
local print = print
local unpack = unpack
local pairs = pairs
local tonumber = tonumber
local type = type
local setmetatable = setmetatable
local rawset = rawset
local rawget = rawget
local require = require
local Utils = require("Utils")
local Classes = require("Classes")
local SQL = require("ljsqlite3")
local ffi = require("ffi")

local nextoid = 0
local database = nil
local statements = {}

local function compile(sql)
	local stmt = statements[sql]
	if stmt then
		stmt:reset()
		return stmt
	end
	
	stmt = database:prepare(sql)
	statements[sql] = stmt
	return stmt
end

local function write_property(oid, kid, value, time)
	local result = compile(
		"INSERT OR REPLACE INTO eav (oid, kid, value, time) VALUES (?, ?, ?, ?)"
		):bind(oid, kid, value, time):step()
	print(result)
end

local function read_property(oid, kid)
	local row = compile(
		"SELECT value, time FROM eav WHERE oid=? AND kid=?"
		):bind(oid, kid):step()
		
	--print(unpack(row))
end

local tokenMap = {}
setmetatable(tokenMap,
{
	__index = function (self, token)
		compile(
			"INSERT OR IGNORE INTO tokens VALUES (NULL, ?)"
		):bind(token):step()
			
		if (type(token) == "number") then
			-- number -> token
			
			local row = compile(
				"SELECT value FROM tokens WHERE id = ?"
			):bind(token):step()
			
			local value = tonumber(row[1])
			print(value, token)
			rawset(tokenMap, token, value)
			rawset(tokenMap, value, token)
			
			return value
		else
			-- token -> number
			
			local row = compile(
				"SELECT id FROM tokens WHERE value = ?"
			):bind(token):step()
			
			local value = tonumber(row[1])
			print(token, value)
			rawset(tokenMap, token, value)
			rawset(tokenMap, value, token)
			
			return value
		end
	end
})

local function get_property_type(class, name)
	while class do
		local p = class.properties
		if p then
			local t = p[name]
			if t then
				return t
			end
		end
		
		class = class.superclass
	end
end

local function get_method(class, name)
	while class do
		local ms = class.methods
		if ms then
			local m = ms[name]
			if m then
				return m
			end
		end
		
		class = class.superclass
	end 
end

local function get_datum(class, oid, name)
	local t = get_property_type(class, name)
	if not t then
		return nil
	end
	
	local datum = 
	{
		type = t,
		oid = oid,
		name = name,
		kid = tokenMap[name],
		value = t.default()
	}
	
	return datum 
end

local function put_datum(datum, value)
	datum.value = value
	local str = datum.type.marshal(datum)
	print("Set "..datum.oid.."."..datum.kid.."("..datum.name..") to "..str)
	
	compile(
		"INSERT OR REPLACE INTO eav (oid, kid, value, time) VALUES (?, ?, ?, ?)"
		):bind(datum.oid, datum.kid, str, 0):step()
end

local function get_class_of_oid(oid)
	local row = compile(
		"SELECT value FROM eav WHERE oid=? AND kid=?"
		):bind(oid, tokenMap["Class"]):step()

	if not row then
		return nil
	end
	
	local classname = tokenMap[tonumber(row[1])]
	return Classes[classname]
end

local function new_object_proxy(oid)
	local class = get_class_of_oid(oid)
	Utils.Assert(class, "oid ", oid, " has no class!")
	
	local dirty = false
	
	local methodcache = {}
	local datumcache = {}
	
	local object = {}
	local metatable
	
	metatable =
	{
		__index = function (self, key)
			local c = methodcache[key]
			if c then
				return c
			end
			
			c = datumcache[key]
			if c then
				return c.value
			end

			c = metatable[key]
			if c then
				methodcache[key] = c
				return c
			end
			
			c = get_method(class, key)
			if c then
				methodcache[key] = c
				return c
			end
			
			c = get_datum(class, oid, key)
			if c then
				datumcache[key] = c
				return c.value
			end
			
			Utils.FatalError("Unknown method or property '", key, "' on ", class.name, "#", oid)
		end,
		
		__newindex = function (self, key, value)
			-- Ensure the datum is cached.
			if not datumcache[key] then
				datumcache[key] = get_datum(class, oid, key)
			end
			
			-- Set the local copy.
			rawset(self, key, value)
			dirty = true
		end,
		
		Oid = oid,
		
		rollback = function ()
			for k, v in pairs(object) do
				object[k] = nil
			end
		end,
		
		commit = function ()
			for k, v in pairs(object) do
				local datum = datumcache[k]
				if not datum then
					Utils.FatalError("Property '", k, "' on oid ", oid, " has modified value but has not been loaded")
				end
				
				put_datum(datum, v)
				object[k] = nil
			end
		end
	}
	
	setmetatable(object, metatable)
	return object
end

local function create_object(oid, class)
	if (type(class) == "string") then
		local c = Classes[class]
		if not c then
			error("'"..class.."' is not a valid class name")
		end
		class = c
	end
	
	compile(
		"INSERT OR REPLACE INTO eav (oid, kid, value, time) VALUES (?, ?, ?, ?)"
		):bind(oid, tokenMap["Class"], tokenMap[class.name], 0):step()
	
	return new_object_proxy(oid, class)
end

return
{
	Connect = function (filename)
		database = SQL.open(filename)
	end,

	Disconnect = function ()
		if database then
			database:close()
			database = nil
		end
	end,

	Begin = function ()
		compile("BEGIN"):step()
	end,

	Commit = function ()
		compile("COMMIT"):step()
	end,

	Rollback = function ()
		compile("ROLLBACK"):step()
	end,

	Init = function ()
		local dbinit = Utils.LoadFile("dbinit.sql")
		database:exec(dbinit)
	end,

	TokenMap = tokenMap,
	
	Open = function ()
		local row = compile("SELECT MAX(oid) FROM eav"):step()
		nextoid = tonumber(row[1])
		if not nextoid then
			nextoid = 1
		end
	end,

	CreateWithOid = create_object,
	
	Create = function (class)
		if not oid then
			oid = nextoid
			nextoid = nextoid + 1
		end		
		
		return create_object(oid, class)
	end,
	
	Object = new_object_proxy
}
