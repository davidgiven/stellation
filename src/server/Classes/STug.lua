local math_random = math.random
local require = require
local Type = require("Type")
local Utils = require("Utils")
local L = Utils.Unindent
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

return
{
	name = "STug",
	superclass = require("Classes.SShip"),
	
	statics =
	{
		Name = "Tug",
		Description = L[[
			Tugs are small, powerful craft used to tow otherwise unpowered vessels
			and other artifacts.
		]],
		Mass = 1000.0,
		MaxDamage = 1000.0,
		BuildCostM = 3000.0,
		BuildCostA = 8000.0,
		BuildCostO = 1000.0,
		BuildTime = 2.0,
		MaintenanceCostM = 0.0,
		MaintenanceCostA = 4.0,
		MaintenanceCostO = 1.0,
	},
	
	properties =
	{
	},
	
	methods =
	{
	}
}
