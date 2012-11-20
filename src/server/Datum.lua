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
		if not t then
			return t
		end
		
		local tablename = create_eav_table(t, name)

		local kid = Tokens[name]
		
		local function dirty()
			SQL("DELETE FROM seenby WHERE oid=? AND kid=?")
				:bind(oid, kid):step()
		end
		
		SQL("INSERT OR IGNORE INTO eav (oid, kid) VALUES (?, ?)")
			:bind(oid, kid):step()
			
		local datum = 
		{
			type = t,
			oid = oid,
			name = name,
			kid = kid,
			
			IsSet = function()
				local isset = SQL(
					"SELECT COUNT(*) FROM "..tablename.." WHERE oid = ?"
					):bind(oid):step()
				
				return isset[1] ~= 0
			end,
			
			Get = function()
				return t.Get(tablename, oid, dirty)
			end,
			
			Set = function(value)
				t.Set(tablename, oid, value)
				dirty()
			end,
			
			Export = function()
				local f = t.Export or t.Get
				return f(tablename, oid)
			end,
			
			Synced = function()
				SQL("INSERT OR REPLACE INTO seenby (oid, kid, cookie) VALUES (?, ?, ?)")
					:bind(oid, kid, G.CurrentCookie):step()
			end 
		}
		
		return datum 
	end,
}
