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
	local o = Datastore.Object(msg.oid)
	o:CheckClass("SDeployable")
	o:CheckManipulatableBy(player)
	local star = o:CheckInOrbit()

	Log.G("deploying ", o.Oid)
	if not o.Deployed then
		return {
			result = "UnitAlreadyMothballed"
		}
	end
	
	o:Mothball()
	star:Log(nil, "unit ", o.Oid, " has been mothballed.")
	
	return {
		result = "OK"
	}
end
