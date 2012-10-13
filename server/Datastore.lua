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
local SQL = require("ljsqlite3")
local ffi = require("ffi")

module("Datastore")

local nextoid = 0
local database = nil
local statements = {}
local tokens

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

function Connect(filename)
	database = SQL.open(filename)
end

function Disconnect()
	if database then
		database:close()
		database = nil
	end
end

function Begin()
	compile("BEGIN"):step()
end

function Commit()
	compile("COMMIT"):step()
end

function Rollback()
	compile("ROLLBACK"):step()
end

function Init()
	local dbinit = Utils.LoadFile("dbinit.sql")
	database:exec(dbinit)
end

function Open()
	local row = compile("SELECT MAX(oid) FROM eav"):step()
	nextoid = tonumber(row[1])
	if not nextoid then
		nextoid = 1
	end
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

function SetTokens(tokenmap)
	compile("BEGIN"):step()
	
	for token in pairs(tokenmap) do
		compile(
			"INSERT OR IGNORE INTO tokens VALUES (NULL, ?)"
		):bind(token):step()
		
		local row = compile(
			"SELECT * FROM tokens WHERE value = ?"
		):bind(token):step()
		
		local value = tonumber(row[1])
		tokenmap[token] = value
		tokenmap[value] = token
	end
	
	compile("COMMIT"):step()
	
	tokens = tokenmap
end

local Classes =
{
	SUniverse = require("classes.SUniverse"),
	SGalaxy = require("classes.SGalaxy"),
	SStar = require("classes.SStar")
}

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
		kid = tokens[name],
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
		):bind(oid, tokens["Class"]):step()
	
	local classname = tokens[tonumber(row[1])]
	return Classes[classname]
end

function Object(oid)
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

function Create(oid, class)
	if not oid then
		oid = nextoid
		nextoid = nextoid + 1
	end
	
	if (type(class) == "string") then
		local c = Classes[class]
		if not c then
			error("'"..class.."' is not a valid class name")
		end
		class = c
	end
	
	compile(
		"INSERT OR REPLACE INTO eav (oid, kid, value, time) VALUES (?, ?, ?, ?)"
		):bind(oid, tokens["Class"], tokens[class.name], 0):step()
	
	return Object(oid, class)
end

