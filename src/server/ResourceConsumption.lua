local Utils = require("Utils")
local Immutable = require("Immutable")
local Database = require("Database")
local Datastore = require("Datastore")
local SQL = Database.SQL
local Socket = require("socket")
local Log = require("Log")
local P = require("P")
local Tokens = require("Tokens")

local function fastconsume(oid, oldtime, newtime)
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
	
	local delta = newtime - oldtime
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
		return newtime
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
	
	return oldtime + t
end

local function consume_from_star(star, m, a, o)
	Log.G("consuming ", m, ", ", a, ", ", o, " from star ", star.Oid)
	
	local sm = star.ResourcesM - m
	local sa = star.ResourcesA - a
	local so = star.ResourcesO - o
	
	if (sm < 0) then
		m = -sm
		sm = 0
	else
		m = 0
	end
	
	if (sa < 0) then
		a = -sa
		sa = 0
	else
		a = 0
	end
	
	if (so < 0) then
		o = -so
		so = 0
	else
		o = 0
	end
	
	star.ResourcesM = sm
	star.ResourcesA = sa
	star.ResourcesO = so
	return m, a, o
end

local function consume_from_cargoship(ship, m, a, o)
	Log.G("consuming ", m, ", ", a, ", ", o, " from cargoship ", ship.Oid)
	
	local sm = ship.CargoM - m
	local sa = ship.CargoA - a
	local so = ship.CargoO - o
	
	if (sm < 0) then
		m = -sm
		sm = 0
	else
		m = 0
	end
	
	if (sa < 0) then
		a = -sa
		sa = 0
	else
		a = 0
	end
	
	if (so < 0) then
		o = -so
		so = 0
	else
		o = 0
	end
	
	ship.CargoM = sm
	ship.CargoA = sa
	ship.CargoO = so
	return m, a, o
end

local function slowconsume(star, object, oldtime, newtime)
	Log.G("per-unit resource consumption for ", object.Oid)
	
	local delta = newtime - oldtime
	local mm = object.MaintenanceCostM * delta
	local ma = object.MaintenanceCostA * delta
	local mo = object.MaintenanceCostO * delta

	local m, a, o = consume_from_star(star, mm, ma, mo)
	if (m <= 0) and (a <= 0) and (o <= 0) then
		return
	end
	
	if object:IsA("SFleet") then
		Log.G("object ", object.Oid, " is a fleet, checking cargoships")
		for ship in object.Contents:Iterate() do
			if ship:IsA("SCargoship") then
				m, a, o = consume_from_cargoship(ship, m, a, o)
				if (m <= 0) and (a <= 0) and (o <= 0) then
					return
				end
			end
		end
	end
	
	-- This object starved. Calculate when based on the amount of resources
	-- it failed to consume in this delta.
	
	local tm = m / object.MaintenanceCostM
	local ta = a / object.MaintenanceCostA
	local to = o / object.MaintenanceCostO
	local t = math.max(tm, ta, to)
	local starvetime = newtime - t
	
	Log.G("object ", object.Oid, " starved at ", starvetime, "!")
	object:Starve(starvetime)
end

return
{
	ConsumeTo = function(timestamp)
		local oldtime = tonumber(P.timestamp)
		local delta = timestamp - oldtime
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
			
			local consumedto = fastconsume(tonumber(oid), oldtime, timestamp)
			if (consumedto < timestamp) then
				local star = Datastore.Object(oid)
				
				for o in star.Contents:Iterate() do
					slowconsume(star, o, consumedto, timestamp)
				end
			end
		end
		
		P.timestamp = timestamp
	end
}
