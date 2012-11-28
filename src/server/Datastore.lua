local Utils = require("Utils")
local Log = require("Log")
local Database = require("Database")
local SQL = Database.SQL
local Tokens = require("Tokens")
local Datum = require("Datum")
local G = require("G")

local proxies = {}

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

local function get_class_of_oid(oid)
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
	if not oid then
		return nil
	end
	
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
	
	local function getdatumwithdefaultvalue(key)
		local c = Datum.Lookup(class, oid, key)
		if c then
			datumcache[key] = c
			
			if not c.type.isaggregate and not c.IsSet() then
				local defaultc = get_method_or_static(class, key)
				if defaultc then
					c.Set(defaultc)
				end
			end
			
			return c
		end
		return nil
	end
	
	metatable =
	{
		__index = function (self, key)
			local c = datumcache[key]
			if c then
				return c.Get()
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
			
			c = getdatumwithdefaultvalue(key)
			if c then
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
		
		__get_datum = function (self, key)
			return getdatumwithdefaultvalue(key)
		end,
		
		__flush_caches = function (self)
			methodcache = {}
			datumcache = {}
		end,
		
		Oid = tonumber(oid),
	}
	
	setmetatable(object, metatable)
	return object
end

local function create_object(class)
	if (type(class) == "string") then
		local c = get_class(class)
		if not c then
			error("'"..class.."' is not a valid class name")
		end
		class = c
	end

	-- eav_Class must go first, as adding an entry here creates the class and
	-- defines the oid.
		
	SQL(
		"INSERT INTO eav_Class (value) VALUES (?)"
		):bind(Tokens[class.name]):step()
		
	local row = SQL("SELECT last_insert_rowid()"):step()
	Utils.Assert(row, "last_insert_rowid() returned nil?")
	local oid = tonumber(row[1])
	
	SQL(
		"INSERT OR REPLACE INTO eav (oid, kid) VALUES (?, ?)"
		):bind(oid, Tokens["Class"]):step()
		
	return new_object_proxy(oid, class)
end

return
{
	Connect = function (filename)
		Database.Connect(filename)
	end,

	Disconnect = function ()
		Database.Disconnect(filename)
	end,

	Begin = function ()
		Database.Begin()
	end,

	Commit = function ()
		Database.Commit()
	end,

	Rollback = function ()
		Database.Rollback()
		
		-- Flush all proxies (as we might have stale cached data).
		for _, o in pairs(proxies) do
			o:__flush_caches()
		end
	end,

	Create = function (class)
		local o = create_object(class)
		proxies[o.Oid] = o
		return o
	end,
	
	Object = function (oid)
		local p = proxies[oid]
		if p then
			return p
		end

		p = new_object_proxy(oid)
		if p then
			proxies[oid] = p
		end
		return p
	end,
	
	DoesObjectExist = function (oid)
		local c = get_class_of_oid(oid)
		return not not c
	end,
	
	Destroy = function (o)
		o:__flush_caches()
		local oid = o.Oid
		
		SQL("DELETE FROM eav_Class WHERE oid=?"):bind(oid):step()
	end
}
