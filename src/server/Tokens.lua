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

local tokenMap = {}
setmetatable(tokenMap,
{
	__index = function (self, v)
		if (type(v) == "number") then
			-- number -> token
			
			local value = v
			local row = SQL(
				"SELECT value FROM tokens WHERE id = ?"
			):bind(value):step()
			
			local token = row[1]
			--Log.D("token '", token, "' has id ", value)
			rawset(tokenMap, token, value)
			rawset(tokenMap, value, token)
			
			return token
		else
			-- token -> number

			local token = v
			Utils.Assert(not tonumber(token), "tokens should be alphanumeric!")
						
			SQL(
				"INSERT OR IGNORE INTO tokens VALUES (NULL, ?)"
			):bind(token):step()
				
			local row = SQL(
				"SELECT id FROM tokens WHERE value = ?"
			):bind(token):step()
			
			local value = tonumber(row[1])
			--Log.D("token '", token, "' has id ", value)
			rawset(tokenMap, token, value)
			rawset(tokenMap, value, token)
			
			return value
		end
	end
})

return tokenMap
