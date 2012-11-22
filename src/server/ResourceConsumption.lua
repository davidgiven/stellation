local Utils = require("Utils")
local Immutable = require("Immutable")
local Database = require("Database")
local Datastore = require("Datastore")
local SQL = Database.SQL
local Socket = require("socket")
local Log = require("Log")
local P = require("P")

return
{
	ConsumeTo = function(timestamp)
		local oldtime = tonumber(P.timestamp)
		local delta = (timestamp - oldtime) / 3600
		Log.G("consuming from ", oldtime, " to ", timestamp, " (", delta, " hours)")
		
		P.timestamp = timestamp
	end
}
