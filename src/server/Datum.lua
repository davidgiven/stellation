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

local nextoid = 0

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

local function create_eav_table(type, name)
	local tablename = "eav_"..name
	SQL(
		"CREATE TABLE IF NOT EXISTS "..tablename.." (oid INTEGER PRIMARY KEY, value "..type.sqltype..", time INTEGER)"
	):step()
	
	return tablename
end

return
{
	Get = function (class, oid, name)
		local t = get_property_type(class, name)
		Utils.Assert(t, "property ", name, " is not defined on class ", class.name)
		
		local datum = 
		{
			type = t,
			oid = oid,
			name = name,
			kid = Tokens[name],
		}
		
		local tablename = create_eav_table(t, name)
		local row = SQL(
			"SELECT value FROM "..tablename.." WHERE oid=?"
			):bind(oid):step()
			
		if row then
			t.unmarshal(datum, row[1])
		else
			datum.value = t.default()
		end
		
		return datum 
	end,

	Put = function (datum, value)
		datum.value = value
		local str = datum.type.marshal(datum)
		
		local tablename = create_eav_table(datum.type, datum.name)
		SQL(
			"INSERT OR REPLACE INTO "..tablename.." (oid, value, time) VALUES (?, ?, ?)"
			):bind(datum.oid, str, 0):step()
	end
}
