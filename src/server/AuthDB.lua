local Utils = require("Utils")
local Log = require("Log")
local SQL = require("ljsqlite3")
local Datastore = require("Datastore")
local Database = require("Database")
local SQL = Database.SQL

local authentications = {}

local function create_cookie()
	while true do
		local ss = {}
		for i = 1, 32 do
			ss[#ss+1] = string.char(math.random(26) + 64)
		end
		local s = table.concat(ss)
		
		if not authentications[s] then
			return s
		end
	end
end

return
{
	Authenticate = function (email, password)
		local row = SQL(
			"SELECT oid FROM players WHERE email = ?"
			):bind(email):step()
			
		if not row then
			Log.C("user ", email, " does not exist")
			return nil
		end
	
		local oid = tonumber(row[1])
		local player = Datastore.Object(oid)
		local cookie = create_cookie()
		authentications[cookie] = player
		Log.C("new auth cookie ", cookie, " for player ", email)
		
		return cookie, player
	end,
	
	VerifyAuthCookie = function (cookie)
		return authentications[cookie]
	end
}
