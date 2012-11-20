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
local GameCommands = require("GameCommands")
local Timers = require("Timers")
local Socket = require("socket")
local AuthDB = require("AuthDB")

local pscopes_table_inited = false

local function init_pscopes_table()
	SQL("DELETE FROM pscopes"):step()
	
	for kid, type in pairs(Classes.properties) do
		SQL("INSERT INTO pscopes (kid, scope) VALUES (?, ?)")
			:bind(kid, type.scope):step()
	end
	pscopes_table_inited = true
end

local function synchronise(visibilitymap, player)
	if not pscopes_table_inited then
		init_pscopes_table()
	end
	
	local cookieid = AuthDB.GetCookieID(G.CurrentCookie)
	Log.C("calculating sync map for ", G.CurrentCookie, " (id ", cookieid, ")")

	local cs = {}
	local count = 0
	
	SQL("DELETE FROM vscopes"):step()
	for object, vscope in pairs(visibilitymap) do
		SQL("INSERT INTO vscopes (oid, scope) VALUES (?, ?)")
			:bind(object.Oid, vscope):step()
	end
	
	-- GLOBAL = 0,
	-- LOCAL = 1,
	-- SERVERONLY = 2,
	-- PRIVATE = 3,
	--
	-- ?1 = player.oid
	-- ?2 = cookie id

	local query = SQL(	
		[[
			SELECT eav.oid, eav.kid FROM eav
			INNER JOIN vscopes ON (vscopes.oid == eav.oid) 
			INNER JOIN pscopes ON (pscopes.kid == eav.kid)
			WHERE
				NOT EXISTS (
					SELECT cookie FROM seenby WHERE
						(seenby.oid == eav.oid) AND
						(seenby.kid == eav.kid) AND
						(seenby.cookie == ?2)
				)
				AND 
				(
					(
						-- Any visible object owned by the player is fully
						-- visible.
						
						(pscopes.scope != 2) AND
						EXISTS (
							SELECT value FROM eav_Owner
								WHERE (eav_Owner.oid == eav.oid) AND
									(eav_Owner.value == ?1)
						)
					)
					OR
					(
						-- Objects being seen globally only have their global
						-- properties visible.
						
						(vscopes.scope == 0) AND
						(pscopes.scope == 0)
					)
					OR
					(
						-- Objects being seen locally have their global and
						-- local properties visible.
						
						(vscopes.scope == 1) AND
						(pscopes.scope IN (0, 1))
					)
				)
		]]
	)  
	
	query:bind(player.Oid, cookieid)
	local results, resultscount = query:resultset()
	 
	Log.C("collating changed properties")

	for i = 1, resultscount do
		local oid = tonumber(results[1][i])
		local kid = tonumber(results[2][i])
		local type = Classes.properties[kid]
		local name = Tokens[kid]
		
		local object = Datastore.Object(oid)
		local scope = visibilitymap[object]

		local datum = object:__get_datum(name)
		datum.Synced(cookieid)
		local c = cs[oid]
		if not c then
			c = {}
			cs[oid] = c
		end
			
		c[name] = datum.Export(name)
		count = count + 1 
		
		result = query:step()
	end
	
	return cs, count
end

return function (msg)
	local cookie = msg.cookie
	if not cookie then
		return { result = "MalformedCommand", extra = "No cookie specified" }
	end
	local player = AuthDB.VerifyAuthCookie(cookie)
	if not player then
		return { result = "AuthenticationFailed" }
	end
	G.CurrentCookie = cookie
	
	Log.G("running pending timers")
	if Timers.RunNextPendingTimer() then
		while Timers.RunNextPendingTimer() do
		end
		
		Datastore.Commit()
		Datastore.Begin()
	end

	local cmd = GameCommands[msg.gcmd]
	if not cmd then
		return { result = "BadCommand" }
	end
	Log.G("running game command ", msg.gcmd)
	local e, result = pcall(
		function()
			return cmd(player, msg)
		end
	)
	if not e then
		if (type(result) == "string") then
			error(result)
		end
	end
		
	Log.G("calculating visibility map")
	local visibilitymap = player:CalculateVisibilityMap()
	player.VisibleObjects:FromLua(visibilitymap)
	
	Log.G("calculation done")
	local sync, count = synchronise(visibilitymap, player)
	Log.G(count, " changed properties")
	
	result.time = Socket.gettime()
	result.changed = sync
	return result
end
