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
		
		RestMass = 1000.0,
		MaxDamage = 300.0,
		BuildCostM = 5000.0,
		BuildCostA = 10000.0,
		BuildCostO = 1000.0,
		BuildTime = 3.0,
		MaintenanceCostM = 0.0,
		MaintenanceCostA = 2.0,
		MaintenanceCostO = 1.0,
		CargoM = 0,
		CargoA = 0,
		CargoO = 0
	},
	
	properties =
	{
		CargoM = Type.Number(LOCAL),
		CargoA = Type.Number(LOCAL),
		CargoO = Type.Number(LOCAL)
	},
	
	methods =
	{
		Init = super.methods.Init,
		
		LoadUnload = function(self, m, a, o)
			local star = self.Location.Location
			local rm = star.ResourcesM - m
			local ra = star.ResourcesA - a
			local ro = star.ResourcesO - o
			local cm = self.CargoM + m
			local ca = self.CargoA + a
			local co = self.CargoO + o
			
			if (rm < 0) or (ra < 0) or (ro < 0) or
			   (cm < 0) or (ca < 0) or (co < 0) then
			   	error { result = "InsufficientResources" }
			end
			
			star.ResourcesM = rm
			star.ResourcesA = ra
			star.ResourcesO = ro
			self.CargoM = cm
			self.CargoA = ca
			self.CargoO = co
		end
	}
}
