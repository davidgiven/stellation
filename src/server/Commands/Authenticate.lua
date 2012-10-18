local Datastore = require("Datastore")
local Database = require("Database")
local Log = require("Log")
local SQL = Database.SQL

return function (msg)
	local row = SQL(
		"SELECT oid FROM players WHERE email = ?"
		):bind(msg.email):step()
		
	if not row then
		Log.C("user ", msg.email, " does not exist")
		return { result = "AuthenticationFailure" }
	end
	
	Log.C("user ", msg.email, " has oid ", row[1])
	local player = Datastore.Object(row[1])
	
	if (player.Password == msg.password) then
		Log.C("user authenticates")
		return
		{
			result = "OK",
			oid = player.Oid
		}
	else
		Log.C("user has wrong password")
		return
		{
			result = "AuthenticationFailure"
		}
	end
end
