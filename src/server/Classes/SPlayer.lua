local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE
local Datastore = require("Datastore")

local super = require("Classes.SObject")

return
{
	name = "SPlayer",
	superclass = super,
	
	statics =
	{
	},
	
	properties =
	{
		Email = Type.String(PRIVATE),
		EmpireName = Type.String(PRIVATE),
		Password = Type.String(SERVERONLY),
		Fleets = Type.ObjectSet(PRIVATE),
		VisibleObjects = Type.ObjectSet(PRIVATE)
	},
	
	methods =
	{
		CalculateVisibilityMap = function (self)
			local map = {}
			
			-- The universe and the galaxy is always visible.
			
			local universe = Datastore.Object(0)
			map[universe] = GLOBAL
			map[universe.Galaxy] = GLOBAL
			
			-- All visible stars are visible.
			
			for star in universe.Galaxy.VisibleStars:Iterate() do
				map[star] = GLOBAL
			end
			
			-- The player itself is visible.
			
			map[self] = PRIVATE
			
			-- Any locations containing a jumpship are visible.
			
			local locations = {}
			for fleet in self.Fleets:Iterate() do
				if (fleet.JumpshipCount > 0) then
					local loc = fleet.Location
					locations[loc] = LOCAL
					map[loc] = LOCAL
				end 
			end
			
			-- Now recursively add the contents of those locations.
			
			for loc in pairs(locations) do
				loc:AddToVisibilityMap(map)
			end
			
			return map
		end
	}
}
