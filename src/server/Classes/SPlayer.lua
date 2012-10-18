local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

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
	}
}
