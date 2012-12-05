local math_random = math.random
local require = require
local Type = require("Type")
local Utils = require("Utils")
local L = Utils.Unindent
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

local super = require("Classes.SFixed")

return
{
	name = "SMessageBuoy",
	superclass = super,
	
	statics =
	{
		Name = "Message Buoy",
		Description = L[[
			Message buoys are simple unpowered capsules used for storing
			small amounts of data. They are ideal for leaving messages for
			people.
		]],
		RestMass = 1.0,
		MaxDamage = 1.0,
		BuildCostM = 100.0,
		BuildCostA = 10.0,
		BuildCostO = 0,
		BuildTime = 0.2,
		MaintenanceCostM = 0.0,
		MaintenanceCostA = 0.0,
		MaintenanceCostO = 0.0,
	},
	
	properties =
	{
	},
	
	methods =
	{
		Init = super.methods.Init
	}
}
