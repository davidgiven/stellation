local Datastore = require("Datastore")
local Database = require("Database")
local Log = require("Log")
local WorldCreation = require("WorldCreation")
local SQL = Database.SQL
local Tokens = require("Tokens")
local G = require("G")
local AuthDB = require("AuthDB")
local Classes = require("Classes")
local Type = require("Type")

return function (player, msg)
	local ship = Datastore.Object(msg.oid)
	ship:CheckClass("SCargoship")
	ship:CheckManipulatableBy(player)

	local m = tonumber(msg.m)
	local a = tonumber(msg.a)
	local o = tonumber(msg.o)
	Log.G("cargoship load/unload; m=", m, " a=", a, " o=", o)
	ship:LoadUnload(m, a, o) 
	
	return {
		result = "OK"
	}
end
