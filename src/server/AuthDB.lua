local Utils = require("Utils")
local Log = require("Log")
local SQL = require("ljsqlite3")
local Datastore = require("Datastore")
local Database = require("Database")
local SQL = Database.SQL

local authentications = {}
local authcount = 0

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
		
		while true do
			local cookie = create_cookie()
			if not authentications[cookie] then
				authcount = authcount+1
				authentications[cookie] = {player=player, id=authcount}
				
				Log.C("new auth cookie ", cookie, " (id ", authcount, ") for player ", email)
				
				return cookie, player
			end
		end
	end,
	
	VerifyAuthCookie = function (cookie)
		local p = authentications[cookie]
		if p then
			return p.player
		end
		return nil
	end,
	
	GetCookieID = function (cookie)
		return authentications[cookie].id
	end
}
