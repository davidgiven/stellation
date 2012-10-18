local math_random = math.random
local require = require
local Type = require("Type")
local Utils = require("Utils")
local L = Utils.Unindent
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

local super = require("Classes.SShip")

return
{
	name = "SCargoship",
	superclass = super,
	
	statics = 
	{
		Name = "Cargoship",
		Description = L[[
			Cargo ships can carry large quantities of the three
		    main commodities. They can be used to transfer resources
		    from one star system to another, and are also used to
		    supply vessels in operation.
		]],
		
		Mass = 1000.0,
		MaxDamage = 300.0,
		BuildCostM = 5000.0,
		BuildCostA = 10000.0,
		BuildCostO = 1000.0,
		BuildTime = 3.0,
		MaintenanceCostM = 0.0,
		MaintenanceCostA = 2.0,
		MaintenanceCostO = 1.0,
	},
	
	properties =
	{
		CargoM = Type.Number(LOCAL),
		CargoA = Type.Number(LOCAL),
		CargoO = Type.Number(LOCAL)
	},
	
	methods =
	{
		Init = function (self)
			self.CargoM = 0
			self.CargoA = 0
			self.CargoO = 0
			
			super.methods.Init(self)
		end
	}
}
