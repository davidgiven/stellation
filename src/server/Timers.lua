local Utils = require("Utils")
local Immutable = require("Immutable")
local Database = require("Database")
local Datastore = require("Datastore")
local SQL = Database.SQL
local Socket = require("socket")
local Log = require("Log")

local function settimer(time, object, command)
	SQL("INSERT INTO timers (time, oid, command) VALUES (?, ?, ?)")
		:bind(time, object.Oid, command)
		:step()
	return SQL("SELECT last_insert_rowid()"):step()[1]
end

return
{
	SetTimer = settimer,
	
	SetTimerDelta = function(time, object, command)
		return settimer(Socket.gettime() + time, object, command)
	end,
	
	UnsetTimer = function(timerid)
		SQL("DELETE FROM timers WHERE id = ?")
			:bind(timerid)
			:step()
	end,
	
	RunNextPendingTimer = function()
		local result = SQL("SELECT id, oid, command FROM timers WHERE time < ? ORDER BY time ASC LIMIT 1")
			:bind(Socket.gettime())
			:step()
		if result then
			local timerid = result[1]
			local oid = result[2]
			local command = result[3]
			SQL("DELETE FROM timers WHERE id = ?")
				:bind(timerid)
				:step()
				
			Log.G("timer #", timerid, " oid=", oid, " command=", command)
			local object = Datastore.Object(oid)
			object[command](object)
			return true
		end
		return false
	end
}
