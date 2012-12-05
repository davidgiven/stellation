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
	name = "STug",
	superclass = super,
	
	statics =
	{
		Name = "Tug",
		Description = L[[
			Tugs are small, powerful craft used to tow otherwise unpowered vessels
			and other artifacts.
		]],
		RestMass = 1000.0,
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
		Add = function (self, object)
			super.methods.Add(self, object)
			self.Mass = self.RestMass + object.Mass
			self:AdjustFleetTotals()
		end,
		
		Sub = function (self, object)
			super.methods.Sub(self, object)
			self.Mass = self.RestMass
			self:AdjustFleetTotals()
		end,
		
		FindCargo = function (self)
			local i = self.Contents:Iterate()
			return i()
		end,
	}
}
