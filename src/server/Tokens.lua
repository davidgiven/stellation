local Utils = require("Utils")
local Log = require("Log")
local Database = require("Database")
local SQL = Database.SQL

local tokenMap = {}

local function number_to_token(number)
	local row = SQL(
		"SELECT value FROM tokens WHERE id = ?"
	):bind(number):step()
	
	local token = row[1]
	Log.D("token ", number, " -> '", token, "'")
	tokenMap[token] = number
	tokenMap[number] = token
	
	return token
end

local function token_to_number(token)
	Utils.Assert(not tonumber(token), "tokens should be alphanumeric!")
				
	SQL(
		"INSERT OR IGNORE INTO tokens VALUES (NULL, ?)"
	):bind(token):step()
		
	local row = SQL(
		"SELECT id FROM tokens WHERE value = ?"
	):bind(token):step()
	
	local number = tonumber(row[1])
	Log.D("token '", token, "' -> ", number)
	tokenMap[token] = number
	tokenMap[number] = token
	
	return number
end

setmetatable(tokenMap,
{
	__index = function (self, v)
		if (type(v) == "number") then
			return number_to_token(v)
		else
			return token_to_number(v)
		end
	end
})

Database.AddRollbackNotification(
	function()
		Log.D("flushing token cache")
		for k in pairs(tokenMap) do
			tokenMap[k] = nil
		end
	end
)

return tokenMap
