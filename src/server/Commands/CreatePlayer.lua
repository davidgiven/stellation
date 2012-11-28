local Datastore = require("Datastore")
local Database = require("Database")
local Log = require("Log")
local WorldCreation = require("WorldCreation")
local SQL = Database.SQL
local Timers = require("Timers")

return function (msg)
	Timers.BringUpToDate()
	
	local player, e = WorldCreation.CreatePlayer(msg.name, msg.empire,
		msg.email, msg.password)
	if e then
		return { result = e }
	end
	
	return
	{
		result = "OK",
		oid = player.Oid
	}
end
