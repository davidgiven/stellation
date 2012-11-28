local Utils = require("Utils")
local Immutable = require("Immutable")
local Database = require("Database")
local Datastore = require("Datastore")
local SQL = Database.SQL
local Log = require("Log")
local ResourceConsumption = require("ResourceConsumption")

local function settimer(time, object, command)
	SQL("INSERT INTO timers (time, oid, command) VALUES (?, ?, ?)")
		:bind(time, object.Oid, command)
		:step()
	return SQL("SELECT last_insert_rowid()"):step()[1]
end

local function runnextpendingtimer()
	local result = SQL("SELECT id, oid, command, time FROM timers WHERE time < ? ORDER BY time ASC LIMIT 1")
		:bind(Utils.Time()):step()
	if result then
		local timerid = result[1]
		local oid = result[2]
		local command = result[3]
		local time = tonumber(result[4])
		
		ResourceConsumption.ConsumeTo(Utils.Time());
		
		SQL("DELETE FROM timers WHERE id = ?")
			:bind(timerid)
			:step()
			
		Log.G("timer #", timerid, " oid=", oid, " command=", command)
		local object = Datastore.Object(oid)
		object[command](object)
		return true
	else
		ResourceConsumption.ConsumeTo(Utils.Time())
	end
	return false
end

return
{
	SetTimer = settimer,
	
	SetTimerDelta = function(time, object, command)
		return settimer(Utils.Time() + time, object, command)
	end,
	
	UnsetTimer = function(timerid)
		SQL("DELETE FROM timers WHERE id = ?")
			:bind(timerid)
			:step()
	end,
	
	RunNextPendingTimer = runnextpendingtimer,
	
	BringUpToDate = function()
		Log.G("running pending timers")
		if runnextpendingtimer() then
			while runnextpendingtimer() do
			end
			
			Datastore.Commit()
			Datastore.Begin()
		end	
	end
}
