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
	if o.Deployed then
		return {
			result = "UnitAlreadyDeployed"
		}
	end
	
	o:Deploy()
	star:Log(nil, "unit ", o.Oid, " has been deployed.")
	
	return {
		result = "OK"
	}
end
