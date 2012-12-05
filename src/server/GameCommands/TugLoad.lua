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

	local cargo = Datastore.Object(msg.cargo)
	cargo:CheckClass("SFixed")
	cargo:CheckManipulatableBy(player)
	
	Log.G("loading tug ", tug.Oid)
	if tug:FindCargo() then
		return {
			result = "TugAlreadyLoaded"
		}
	end
	
	local star = tug:FindStar()
	if (star ~= cargo:FindStar()) then
		return {
			result = "NonLocalObject"
		}
	end
	
	cargo:MoveTo(tug)
	star:Log(nil, "unit ", cargo.Oid, " has been loaded onto a tug.")
	
	return {
		result = "OK"
	}
end
