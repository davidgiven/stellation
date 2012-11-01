local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

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
	}
}
