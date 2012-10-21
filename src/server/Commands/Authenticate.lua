local Datastore = require("Datastore")
local Database = require("Database")
local AuthDB = require("AuthDB")
local Log = require("Log")
local SQL = Database.SQL

return function (msg)
	local cookie, player = AuthDB.Authenticate(msg.email)
	
	if cookie then
		Log.C("user authenticates")
		return
		{
			result = "OK",
			oid = player.Oid,
			cookie = cookie
		}
	else
		Log.C("user has wrong password")
		return
		{
			result = "AuthenticationFailure"
		}
	end
end
