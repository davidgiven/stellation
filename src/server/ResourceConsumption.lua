local Utils = require("Utils")
local Immutable = require("Immutable")
local Database = require("Database")
local Datastore = require("Datastore")
local SQL = Database.SQL
local Socket = require("socket")
local Log = require("Log")
local P = require("P")
local Tokens = require("Tokens")

local function fastconsume(oid, delta)
	local query = SQL(
		[[
			SELECT
				SUM(eav_MaintenanceCostM.value),
				SUM(eav_MaintenanceCostA.value),
				SUM(eav_MaintenanceCostO.value)
			FROM eav_Contents
			INNER JOIN eav_MaintenanceCostM
				ON (eav_MaintenanceCostM.oid == eav_Contents.value)
			INNER JOIN eav_MaintenanceCostA
				ON (eav_MaintenanceCostA.oid == eav_Contents.value)
			INNER JOIN eav_MaintenanceCostO
				ON (eav_MaintenanceCostO.oid == eav_Contents.value)
			WHERE (eav_Contents.oid == ?)
		]]
	):bind(oid)
	
	local results = query:step()
	local mm = tonumber(results[1])
	local ma = tonumber(results[2])
	local mo = tonumber(results[3])
	
	Log.G("total maintenance cost for ", oid, " is ", mm, ", ", ma, ", ", mo)
	
	local dmm = mm * delta
	local dma = ma * delta
	local dmo = mo * delta
	
	Log.G("maintenance cost for delta ", delta, " is ", dmm, ", ", dma, ", ", dmo)
	
	local star = Datastore.Object(oid)
	local sm = star.ResourcesM - dmm
	local sa = star.ResourcesA - dma
	local so = star.ResourcesO - dmo
	if (sm >= 0) and (sa >= 0) and (so >= 0) then
		Log.G("meeting maintenance cost from stellar resources")
		star.ResourcesM = sm
		star.ResourcesA = sa
		star.ResourcesO = so
		return delta
	end
	
	local t = delta
	if (sm <= sa) and (sm <= so) then
		t = t - (-sm) / mm
	elseif (sa <= sm) and (sa <= so) then
		t = t - (-sa) / ma
	elseif (so <= sm) and (so <= sa) then
		t = t - (-so) / mo
	end
	
	Log.G("stellar resources exhausted, enough available for ", t)

	dmm = mm * t
	dma = ma * t
	dmo = mo * t
	
	Log.G("consuming partial requirements of ", dmm, ", ", dma, ", ", dmo)
	
	star.ResourcesM = star.ResourcesM - dmm
	star.ResourcesA = star.ResourcesA - dma
	star.ResourcesO = star.ResourcesO - dmo
	
	return t
end

return
{
	ConsumeTo = function(timestamp)
		local oldtime = tonumber(P.timestamp)
		local delta = (timestamp - oldtime) / 3600
		Log.G("consuming from ", oldtime, " to ", timestamp, " (", delta, " hours)")
		
		-- Find all locations which are stars and contain
		local query = SQL(
			[[
				SELECT DISTINCT eav_Class.oid FROM eav_Class
				WHERE
					(eav_Class.value == ?) AND
					EXISTS (
						SELECT eav_Contents.value FROM eav_Contents
						WHERE eav_Contents.oid == eav_Class.oid
					)
			]]
		):bind(Tokens["SStar"])
		
		local results, nrecords = query:resultset()
		for i = 1, nrecords do
			local oid = results[1][i]
			fastconsume(tonumber(oid), delta)
		end
		
		P.timestamp = timestamp
	end
}
