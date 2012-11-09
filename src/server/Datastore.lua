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
local Log = require("Log")
local Database = require("Database")
local SQL = Database.SQL
local Tokens = require("Tokens")
local Datum = require("Datum")
local G = require("G")

local nextoid = 0
local proxies = {}
local dirty = {}

local classes_p
local function get_class(name)
	if not classes_p then
		classes_p = require("Classes")
	end
	return classes_p[name]
end

local function get_method_or_static(class, name)
	while class do
		local ms = class.methods
		if ms then
			local m = ms[name]
			if m then
				return m
			end
		end
		
		ms = class.statics
		if ms then
			local m = ms[name]
			if m then
				return m
			end
		end
		
		class = class.superclass
	end 
end

local functilocal function get_class_of_oid(oid)
	local row = SQL(
		"SELECT value FROM eav_Class WHERE oid=?"
		):bind(oid):step()

	if not row then
		return nil
	end
	
	local tokenid = tonumber(row[1])
	local classname = Tokens[tokenid]
	return get_class(classname)
end

local function new_object_proxy(oid)
	local class = get_class_of_oid(oid)
	Utils.Assert(class, "oid ", oid, " has no class!")
	
	local methodcache = {}
	local datumcache = {}
	
	local object = {}
	local metatable
	
	local function getdatum(key)
		local c = datumcache[key]
		if not c then
			c = Datum.Lookup(class, oid, key)
			if not c then
				Utils.FatalError("Unknown method or property '", key, "' on ", class.name, "#", oid)
			end
			datumcache[key] = c
		end
		
		return c
	end
	
	metatable =
	{
		__index = function (self, key)
			local c = datumcache[key]
			if c then
				return c:Get()
			end

			c = methodcache[key]
			if c then
				return c
			end
			
			c = metatable[key]
			if c then
				methodcache[key] = c
				return c
			end
			
			c = Datum.Lookup(class, oid, key)
			if c then
				datumcache[key] = c
				return c.Get()
			end
			
			c = get_method_or_static(class, key)
			if c then
				methodcache[key] = c
				return c
			end
			
			Utils.FatalError("Unknown method or property '", key, "' on ", class.name, "#", oid)
		end,
		
		__newindex = function (self, key, value)
			methodcache[key] = nil
			getdatum(key).Set(value)
		end,
		
		__export_property = function (self, key)
			return getdatum(key).Export()
		end,
		
		Oid = tonumber(oid),
	}
	
	setmetatable(object, metatable)
	return object
end

local function create_object(oid, class)
	if (type(class) == "string") then
		local c = get_class(class)
		if not c then
			error("'"..class.."' is not a valid class name")
		end
		class = c
	end

	-- eav_Class must go first, as adding an entry here creates the class
		
	SQL(
		"INSERT OR REPLACE INTO eav_Class (oid, value) VALUES (?, ?)"
		):bind(oid, Tokens[class.name]):step()
		
	SQL(
		"INSERT OR REPLACE INTO eav (oid, kid, time) VALUES (?, ?, ?)"
		):bind(oid, Tokens["Class"], G.CanonicalTime):step()

	return new_object_proxy(oid, class)
end

return
{
	Connect = function (filename)
		Database.Connect(filename)

		local row = SQL("SELECT MAX(oid) FROM eav_Class"):step()
		nextoid = tonumber(row[1])
		if not nextoid then
			nextoid = 1
		end
	end,

	Disconnect = function ()
		Database.Disconnect(filename)
	end,

	Begin = function ()
		Database.Begin()
		Utils.Assert(not next(dirty), "start of transaction and dirty object list is not empty")
	end,

	Commit = function ()
		Database.Commit()
	end,

	Rollback = function ()
		Database.Rollback()
	end,

	CreateWithOid = function (oid, class)
		local o = create_object(oid, class)
		proxies[oid] = o
		return o
	end,
	
	Create = function (class)
		local oid = nextoid
		nextoid = nextoid + 1
		
		local o = create_object(oid, class)
		proxies[oid] = o
		return o
	end,
	
	Object = function (oid)
		local p = proxies[oid]
		if p then
			return p
		end
		
		p = new_object_proxy(oid)
		proxies[oid] = p
		return p
	end,
	
	DoesObjectExist = function (oid)
		local c = get_class_of_oid(oid)
		return not not c
	end,
	
	CalculateServerCanonicalTime = function ()
		local t = SQL(
			"SELECT MAX(time) FROM eav"
			):step()[1]
		return tonumber(t)
	end,
}
