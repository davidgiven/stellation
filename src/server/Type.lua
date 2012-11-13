local Utils = require("Utils")
local Immutable = require("Immutable")
local Database = require("Database")
local SQL = Database.SQL
local Tokens = require("Tokens")
local JSON = require("cjson")

local findproxy_p
local function findproxy(oid)
	if not findproxy_p then
		findproxy_p = require("Datastore").Object
	end
	return findproxy_p(oid)
end

local ObjectType =
{
	Set = function (tablename, oid, value)
		local v = nil
		if value then
			v = value.Oid
		end
		SQL(
			"INSERT OR REPLACE INTO "..tablename.." (oid, value) VALUES (?, ?)"
			):bind(oid, v):step()
	end,
	
	Get = function (tablename, oid)
		local row = SQL(
			"SELECT value FROM "..tablename.." WHERE oid=?"
			):bind(oid):step()
		if not row then
			return nil
		else
			return findproxy(tonumber(row[1]))
		end
	end,

	Export = function (tablename, oid)
		local row = SQL(
			"SELECT value FROM "..tablename.." WHERE oid=?"
			):bind(oid):step()
		if not row then
			return JSON.null
		else
			return tonumber(row[1])
		end
	end,
	
	name = "ObjectType",
	isaggregate = false,	
	sqltype = "INTEGER REFERENCES eav_Class(oid)",
	jstype = "object"
}

local TokenType =
{
	Set = function (tablename, oid, value)
		SQL(
			"INSERT OR REPLACE INTO "..tablename.." (oid, value) VALUES (?, ?)"
			):bind(oid, Tokens[value]):step()
	end,
	
	Get = function (tablename, oid)
		local row = SQL(
			"SELECT value FROM "..tablename.." WHERE oid=?"
			):bind(oid):step()
		if not row then
			return nil
		else
			return Tokens[tonumber(row[1])]
		end
	end,
	
	name = "TokenType",
	isaggregate = false,	
	sqltype = "INTEGER REFERENCES tokens(id)",
	jstype = "string"
}

local ObjectSetType =
{
	Set = function (tablename, oid, kname, value)
		Utils.FatalError("cannot assign directly to objectset property")
	end,
	
	Get = function (tablename, oid, dirty)
		return
		{
			IsSet = function (self, value)
				local result = SQL(
					"SELECT value FROM "..tablename.." WHERE oid=? AND value=?"
					):bind(oid, value.Oid):step()
					
				if result then
					return true
				end
				return false
			end,
			
			Add = function (self, value)
				local result = SQL(
					"SELECT value FROM "..tablename.." WHERE oid=? AND value=?"
					):bind(oid, value.Oid):step()
					
				if not result then
					SQL(
						"INSERT INTO "..tablename.." (oid, value) VALUES (?, ?)"
						):bind(oid, value.Oid):step()
					dirty()
				end
			end,

			Sub = function (self, value)
				SQL(
					"DELETE FROM "..tablename.." WHERE oid=? AND value=?"
					):bind(oid, value.Oid):step()
				dirty()
			end,
						
			RandomItem = function (self)
				local column = SQL(
					"SELECT value FROM "..tablename.." WHERE oid = ?"
					):bind(oid):resultset()[1]
				
				return findproxy(tonumber(column[math.random(#column)]))
			end,
			
			ToLua = function (self)
				local column = SQL(
					"SELECT value FROM "..tablename.." WHERE oid = ?"
					):bind(oid):resultset()[1]

				local s = {}
				for _, i in ipairs(column) do
					local o = findproxy(tonumber(i))
					s[o] = true
				end				
				return s
			end,
			
			FromLua = function (self, s)
				local d = false

				-- Read the old value of the set.
								
				local results = SQL(
					"SELECT value FROM "..tablename.." WHERE oid=?"
					):bind(oid):resultset()

				local olds = {}
				if results then
					for _, i in ipairs(results[1]) do
						local o = findproxy(tonumber(i))
						olds[o] = true
					end
				end				
				
				-- Delete any items which are in the old set but not in the
				-- new set.
				
				for k, _ in pairs(olds) do
					if not s[k] then
						SQL(
							"DELETE FROM "..tablename.." WHERE oid=? AND value=?"
							):bind(oid, k.Oid):step()
						d = true
					end
				end
				
				-- Insert any items which are in the new set but not in the
				-- old set.
				
				for k, _ in pairs(s) do
					if not olds[k] then
						SQL(
							"INSERT INTO "..tablename.." (oid, value) VALUES (?, ?)"
							):bind(oid, k.Oid):step()
						d = true
					end
				end
				
				if d then
					dirty()
				end
			end,
			
			Clear = function (self)
				SQL(
					"DELETE FROM "..tablename.." WHERE oid = ?"
					):bind(oid):step()
				dirty()
			end,
			
			Iterate = function (self)
				local query = SQL(
					"SELECT value FROM "..tablename.." WHERE oid = ?"
					):bind(oid)
				local resultset = query:resultset()
				if not resultset then
					return function()
						return nil
					end
				end
				local results = resultset[1]
				local i = 1

				return function ()
					local row = results[i]
					if not row then
						return nil
					end

					i = i + 1					
					return findproxy(tonumber(row))
				end
			end
		}
	end,
	
	Export = function (tablename, oid)
		local result = {}
		
		local query = SQL(
			"SELECT value FROM "..tablename.." WHERE oid = ?"
			):bind(oid)

		while true do
			local row = query:step()
			if not row then
				break
			end
			
			result[#result+1] = tonumber(row[1])
		end
		
		return result
	end,
	
	name = "ObjectSetType",
	isaggregate = true,	
	sqltype = "INTEGER REFERENCES eav_Class(oid)",
	jstype = "objectset"
}

local StringType =
{
	Set = function (tablename, oid, value)
		SQL(
			"INSERT OR REPLACE INTO "..tablename.." (oid, value) VALUES (?, ?)"
			):bind(oid, value):step()
	end,
	
	Get = function (tablename, oid)
		local row = SQL(
			"SELECT value FROM "..tablename.." WHERE oid=?"
			):bind(oid):step()
		if not row then
			return ""
		else
			return row[1]
		end
	end,
		
	name = "StringType",
	isaggregate = false,	
	sqltype = "TEXT",
	jstype = "string"
}

local NumberType =
{
	Set = function (tablename, oid, value)
		SQL(
			"INSERT OR REPLACE INTO "..tablename.." (oid, value) VALUES (?, ?)"
			):bind(oid, value):step()
	end,
	
	Get = function (tablename, oid)
		local row = SQL(
			"SELECT value FROM "..tablename.." WHERE oid=?"
			):bind(oid):step()
		if not row then
			return 0
		else
			return tonumber(row[1])
		end
	end,
		
	name = "NumberType",
	isaggregate = false,	
	sqltype = "REAL",
	jstype = "number"
}

local cache = {}
local function typeinstance(type, scope)
	local key = type.name .. "\n" .. scope
	local c = cache[key]
	if not c then
		c = {
			__index = type,
			scope = scope,
		}
		
		setmetatable(c, c)
		cache[key] = c
	end
	
	return c
end

return
{ 
	GLOBAL = "global",
	LOCAL = "local",
	SERVERONLY = "serveronly",
	PRIVATE = "private",

	Object = function (scope)
		return typeinstance(ObjectType, scope)
	end,
	
	ObjectSet = function (scope)
		return typeinstance(ObjectSetType, scope)
	end,
	
	String = function (scope)
		return typeinstance(StringType, scope)
	end,
	
	Token = function (scope)
		return typeinstance(TokenType, scope)
	end,
	
	Number = function (scope)
		return typeinstance(NumberType, scope)
	end
}