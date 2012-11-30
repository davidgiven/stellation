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

local function create_eav_table(type, name, kid)
	local tablename = "eav_"..name
	local keytype = "PRIMARY KEY"
	if type.isaggregate then
		keytype = "NOT NULL"
	end
	SQL(
		"CREATE TABLE IF NOT EXISTS "..tablename..
			" (oid INTEGER "..keytype.." REFERENCES eav_Class(oid) ON DELETE CASCADE, value "..type.sqltype..")"
	):step()
	
	if type.isaggregate then
		SQL(
			"CREATE INDEX IF NOT EXISTS index_byoid_"..tablename.." ON "..tablename.." (oid)"
			):step()
		SQL(
			"CREATE INDEX IF NOT EXISTS index_byboth_"..tablename.." ON "..tablename.." (oid, value)"
			):step()
	end
	
	SQL(
		"CREATE TRIGGER IF NOT EXISTS "..tablename.."_delete_trigger AFTER DELETE ON "..tablename.." "..
		"FOR EACH ROW "..
		"BEGIN "..
			"DELETE FROM seenby WHERE oid=OLD.oid AND kid="..kid.."; "..
		"END"
		):step()
		
	SQL(
		"CREATE TRIGGER IF NOT EXISTS "..tablename.."_update_trigger AFTER UPDATE ON "..tablename.." "..
		"FOR EACH ROW "..
		"BEGIN "..
			"DELETE FROM seenby WHERE oid=OLD.oid AND kid="..kid.."; "..
		"END"
		):step()
		
	SQL(
		"CREATE TRIGGER IF NOT EXISTS "..tablename.."_insert_trigger AFTER INSERT ON "..tablename.." "..
		"FOR EACH ROW "..
		"BEGIN "..
			"DELETE FROM seenby WHERE oid=NEW.oid AND kid="..kid.."; "..
		"END"
		):step()
		
	return tablename
end

return
{
	Lookup = function (class, oid, name)
		local t = get_property_type(class, name)
		if not t then
			return t
		end
		
		local kid = Tokens[name]
		local tablename = create_eav_table(t, name, kid)
		
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
				return t.Get(tablename, oid)
			end,
			
			Set = function(value)
				t.Set(tablename, oid, value)
			end,
			
			Export = function()
				local f = t.Export or t.Get
				return f(tablename, oid)
			end,
			
			Synced = function(id)
				SQL("INSERT OR REPLACE INTO seenby (oid, kid, cookie) VALUES (?, ?, ?)")
					:bind(oid, kid, id):step()
			end 
		}
		
		return datum 
	end,
}
