local require = require
local Type = require("Type")
local Utils = require("Utils")
local Log = require("Log")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

return
{
	name = "SObject",

	statics =
	{
	},
	
	properties =
	{
		Owner = Type.Object(GLOBAL),
		Class = Type.Token(GLOBAL),
		Contents = Type.ObjectSet(LOCAL),
		Location = Type.Object(GLOBAL),
		Name = Type.String(GLOBAL)
	},
	
	methods =
	{
		Init = function (self)
		end,
		
		Create = function (self, class)
			local o = require("Datastore").Create(class)
			o.Owner = self.Owner
			o:Init()
			self:Add(o)
			return o
		end,
	
	
		Add = function (self, object)
			Utils.Assert(object.Location == nil, "object already belong to something")
			object.Location = self
			self.Contents:Add(object)
		end,
		
		AddToVisibilityMap = function(self, map)
			for o in self.Contents:Iterate() do
				map[o] = LOCAL
				o:AddToVisibilityMap(map)
			end
		end,
		
		-- Checks to make sure that this object is visible to the specified
		-- player.
		
		CheckVisibleTo = function (self, player)
			-- Work up the contents hierarchy until we see a star.
			
			local o = self
			while (o.Class ~= "SStar") do
				o = o.Location
				if not o then
					Log.G("object ", self.Oid, " is invisible because it's not in a star")
					error({ result = "Invisible" })
				end
			end
			
			-- Does this star contain a fleet belonging to the player?
			
			for f in o.Contents:Iterate() do
				if (f.Class == "SFleet") and (f.Owner == player) then
					-- Does this fleet have a jumpship?
					if (f.JumpshipCount > 0) then
						return
					end
				end
			end
			
			-- The unit is not visible.
			
			Log.G("object ", self.Oid, " is invisible because it's not in a star with a jumpship")
			error({ result = "Invisible" })
		end,
		
		-- Checks to make sure that this object is manipulatable by the
		-- specified player. That is: it's visible to the player, and owned
		-- by the player.
		 
		CheckManipulatableBy = function (self, player)
			self:CheckVisibleTo(player)
			
			if (self.Owner ~= player) then
				error({ result = "PermissionDenied" })
			end
		end,
		
		-- Checks to make sure that this object is a member of the specified
		-- class (or a subclass thereof).
		
		CheckClass = function (self, class)
			local classes = require("Classes")
			local c = classes[self.Class]
			local tc = classes[class]
			
			while (c ~= tc) do
				c = c.superclass
				if not c then
					error({ result = "IncorrectClass" })
				end
			end
		end,
	}
}
