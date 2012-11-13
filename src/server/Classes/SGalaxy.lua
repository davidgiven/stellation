local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE
local Datastore = require("Datastore")

return
{
	name = "SGalaxy",
	superclass = require("Classes.SObject"),
	
	statics =
	{
		GalacticRadius = 30,
		NumberOfStars = 200
	},
	
	properties =
	{
		AllLocations = Type.ObjectSet(SERVERONLY),
		VisibleStars = Type.ObjectSet(GLOBAL)
	},
	
	methods =
	{
		GetLocation = function(self, x, y)
			for star in self.AllLocations.Iterate() do
				if (star.X == x) and (star.Y == y) then
					return star
				end
			end
			
			-- Add a new dark star at this location.
			
			local s = Datastore.Create("SStar")
			s.Owner = universe
			s.Name = "Interstellar Space ["..x..", "..y.."]"
			s.X = x 
			s.Y = y
			s.Brightness = 0
			s.AsteroidsC = 0
			s.AsteroidsM = 0

			self.AllLocations:Add(s)
			return s			
		end
	}
}
