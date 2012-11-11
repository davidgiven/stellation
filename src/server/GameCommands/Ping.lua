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
	return {
		result = "OK"
	}
end
