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
	local tug = Datastore.Object(msg.oid)
	tug:CheckClass("STug")
	tug:CheckManipulatableBy(player)

	Log.G("unloading tug ", tug.Oid)
	local cargo = tug:FindCargo()
	if not cargo then
		return {
			result = "TugNotLoaded"
		}
	end
	
	local star = tug:FindStar()
	cargo:MoveTo(star)
	star:Log(nil, "unit ", cargo.Oid, " has been unloaded from a tug.")
	
	return {
		result = "OK"
	}
end
