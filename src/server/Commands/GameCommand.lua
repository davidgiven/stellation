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

local function synchronise(visibilitymap, player)
	Log.C("synchronising client ", G.CurrentCookie)

	local cs = {}
	local count = 0
	for object, scope in pairs(visibilitymap) do
		local class = Classes[object.Class]
		local oid = object.Oid
		
		while class do
			for name, type in pairs(class.properties) do
				if is_property_exported(type.scope, scope, object, player) then
					local datum = object:__get_datum(name)
					if datum.TestAndSetSyncBit() then
						local c = cs[oid]
						if not c then
							c = {}
							cs[oid] = c
						end
						
						c[name] = datum.Export(name)
						count = count + 1 
					end
				end
			end
			
			class = class.superclass
		end
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
	
	result.changed = sync
	return result
end
