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
	name = "SJumpship",
	superclass = require("Classes.SShip"),
	
	statics =
	{
		Name = "Jumpship",
		Description = L[[
			A jumpship is used to carry the spatial flaw used to
			bootstrap wormholes. Its primary function is to carry fleets
			of other ships through interstellar distances. The spatial
			flaw can also be used to send instantaneous communications
			from one jumpship to another, but only ones for which the
			exact resonant frequency of the flaw is known.
		]],
		Mass = 5000.0,
		MaxDamage = 1000.0,
		BuildCostM = 10000.0,
		BuildCostA = 20000.0,
		BuildCostO = 1000.0,
		BuildTime = 5.0,
		MaintenanceCostM = 0.0,
		MaintenanceCostA = 5.0,
		MaintenanceCostM = 2.0
	},
	
	properties =
	{
	},
	
	methods =
	{
	}
}
