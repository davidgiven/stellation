local math_random = math.random
local require = require
local Type = require("Type")
local Utils = require("Utils")
local L = Utils.Unindent
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

local super = require("Classes.SRefinery")

return
{
	name = "SAntimatterDistillery",
	superclass = super,
	
	statics =
	{
		Name = "Antimatter Distillery",
		Description = L[[
			Antimatter distilleries orbit the local star at low altitude and
			use a continuous stream of scoop projectiles to collect plasma
			from the surface. The energy so collected is used to synthesise
			stabilised antimatter, which can be safely stored and used to
			power other units.
		]],
		RestMass = 8000.0,
		MaxDamage = 3000.0,
		BuildCostM = 10000.0,
		BuildCostA = 30000.0,
		BuildCostO = 2000.0,
		BuildTime = 5.0,
		DeployedMaintenanceCostM = 0.0,
		DeployedMaintenanceCostA = 5.0,
		DeployedMaintenanceCostO = 1.0,
	},	
	
	properties =
	{
	},
	
	methods =
	{
		Init = super.methods.Init
	}
}
