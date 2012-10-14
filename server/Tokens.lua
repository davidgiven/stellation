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
	__index = function (self, token)
		SQL(
			"INSERT OR IGNORE INTO tokens VALUES (NULL, ?)"
		):bind(token):step()
			
		if (type(token) == "number") then
			-- number -> token
			
			local row = SQL(
				"SELECT value FROM tokens WHERE id = ?"
			):bind(token):step()
			
			local value = tonumber(row[1])
			Log.D("token '", value, "' has id ", token)
			rawset(tokenMap, token, value)
			rawset(tokenMap, value, token)
			
			return value
		else
			-- token -> number
			
			local row = SQL(
				"SELECT id FROM tokens WHERE value = ?"
			):bind(token):step()
			
			local value = tonumber(row[1])
			Log.D("token '", token, "' has id ", value)
			rawset(tokenMap, token, value)
			rawset(tokenMap, value, token)
			
			return value
		end
	end
})

return tokenMap
