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
local G = require("G")

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
	local keytype = "PRIMARY KEY"
	if type.isaggregate then
		keytype = "NOT NULL"
	end
	SQL(
		"CREATE TABLE IF NOT EXISTS "..tablename..
			" (oid INTEGER "..keytype.." REFERENCES eav_Class(oid), value "..type.sqltype..")"
	):step()
	
	if type.isaggregate then
		SQL(
			"CREATE INDEX IF NOT EXISTS index_byoid_"..tablename.." ON "..tablename.." (oid)"
			):step()
		SQL(
			"CREATE INDEX IF NOT EXISTS index_byboth_"..tablename.." ON "..tablename.." (oid, value)"
			):step()
	end
	
	return tablename
end

return
{
	Lookup = function (class, oid, name)
		local t = get_property_type(class, name)
		Utils.Assert(t, "property ", name, " is not defined on class ", class.name)
		
		local tablename = create_eav_table(t, name)

		local kid = Tokens[name]
		local datum = 
		{
			type = t,
			oid = oid,
			name = name,
			kid = kid,
			
			Get = function()
				return t.Get(tablename, oid)
			end,
			
			Set = function(value)
				t.Set(tablename, oid, value)
				
				SQL(
					"INSERT OR REPLACE INTO eav (oid, kid, time) VALUES (?, ?, ?)"
					):bind(oid, kid, G.CanonicalTime):step()
			end
		}
		
		return datum 
	end,
}
