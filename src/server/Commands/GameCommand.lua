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

local function is_property_exported(pscope, vscope, object, player)
	-- The player can see the object, somehow. What properties do we export?
	-- If the object belongs to the player, export everything (except the
	-- server-only ones).

	if (object.Owner == player) and (pscope ~= Type.SERVERONLY) then
		return true
	end
	
	-- This must be someone else's object. If we seeing it via a global view,
	-- then we can only see global properties.
	
	if (vscope == Type.GLOBAL) and (pscope == Type.GLOBAL) then
		return true
	end
	
	-- If via a local view, the global or local objects.
	
	if (vscope == Type.LOCAL) and
			((pscope == Type.GLOBAL) or (pscope == Type.LOCAL)) then
		return true
	end
	
	-- Otherwise, the player doesn't get to see the property.
	
	return false
end

local function synchronise(ctime, visibilitymap, player)
	Log.C("synchronising client with time ", ctime,
		" against server with time ", G.CanonicalTime)

	local cs = {}
	local count = 0
	for o, scope in pairs(visibilitymap) do
		local statement = SQL(
			"SELECT oid, kid, time FROM eav WHERE oid=? AND time>?"
			):bind(o.Oid, ctime)
	
		while true do
			local row = statement:step()
			if not row then
				break
			end
			
			local oid = tonumber(row[1])
			local kid = tonumber(row[2])
			local object = Datastore.Object(oid)
			
			local t = Classes.properties[kid]
			if is_property_exported(t.scope, scope, object, player) then
				local c = cs[oid]
				if not c then
					c = {}
					cs[oid] = c
				end
				
				local kname = Tokens[kid]
				c[kname] = object:__export_property(kname)
				count = count + 1 
			end
		end
	end
	
	return cs, count
end

return function (msg)
	local ctime = msg.time
	if not ctime then
		return { result = "MalformedCommand", extra = "No time specified" }
	end
	local cookie = msg.cookie
	if not cookie then
		return { result = "MalformedCommand", extra = "No cookie specified" }
	end
	local player = AuthDB.VerifyAuthCookie(cookie)
	if not player then
		return { result = "AuthenticationFailed" }
	end
	
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
	Log.G("calculation done")
	local sync, count = synchronise(ctime, visibilitymap, player)
	Log.G(count, " changed properties")
	
	result.changed = sync
	return result
end
