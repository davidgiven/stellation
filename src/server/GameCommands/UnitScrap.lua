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
	local unit = Datastore.Object(msg.oid)
	unit:CheckClass("SUnit")
	unit:CheckManipulatableBy(player)

	Log.G("scrapping unit ", unit.Oid)
	local star = unit:FindStar()
	star:Log(nil, "unit ", unit.Name, " has been scrapped.")
	unit:Scrap() 
	
	return {
		result = "OK"
	}
end
