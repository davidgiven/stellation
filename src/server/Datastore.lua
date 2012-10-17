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
local Classes = require("Classes")
local Database = require("Database")
local SQL = Database.SQL
local Tokens = require("Tokens")
local Datum = require("Datum")

local nextoid = 0
local proxies = {}
local dirty = {}

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

local function get_class_of_oid(oid)
	local row = SQL(
		"SELECT value FROM eav_Class WHERE oid=?"
		):bind(oid):step()

	if not row then
		return nil
	end
	
	local tokenid = tonumber(row[1])
	local classname = Tokens[tokenid]
	return Classes[classname]
end

local function new_object_proxy(oid)
	local class = get_class_of_oid(oid)
	Utils.Assert(class, "oid ", oid, " has no class!")
	
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
			
			c = Datum.Get(class, oid, key)
			if c then
				datumcache[key] = c
				return c.value
			end
			
			Utils.FatalError("Unknown method or property '", key, "' on ", class.name, "#", oid)
		end,
		
		__newindex = function (self, key, value)
			-- Ensure the datum is cached.
			if not datumcache[key] then
				datumcache[key] = Datum.Get(class, oid, key)
			end
			
			-- Set the local copy.
			rawset(self, key, value)
			dirty[object] = true
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
				
				Datum.Put(datum, v)
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
	
	SQL(
		"INSERT OR REPLACE INTO eav_Class (oid, value, time) VALUES (?, ?, ?)"
		):bind(oid, Tokens[class.name], 0):step()
	
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
		local count = 0
		for o, _ in pairs(dirty) do
			o:commit()
			count = count + 1
		end
		Log.D("committed ", count, " dirty objects")
		dirty = {}
		Database.Commit()
	end,

	Rollback = function ()
		Log.D("rolling back transaction")
		for o, _ in pairs(dirty) do
			o:rollback()
		end
		dirty = {}
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
		proxies[p] = oid
		return p
	end,
	
	DoesObjectExist = function (oid)
		local c = get_class_of_oid(oid)
		return not not c
	end,
}
