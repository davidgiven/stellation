local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE
local Log = require("Log")
local Timers = require("Timers")

local super = require("Classes.SObject")

return
{
	name = "SFleet",
	superclass = super,
	
	statics =
	{
		TargetX = 0,
		TargetY = 0,
		AntimatterJumpCost = 1,
		JumpSpeed = 1,
		Mass = 0,
		MaintenanceCostM = 0,
		MaintenanceCostA = 0,
		MaintenanceCostO = 0,
	},
	
	properties =
	{
		JumpshipCount = Type.Number(LOCAL),
		TargetX = Type.Number(PRIVATE),
		TargetY = Type.Number(PRIVATE),
		Mass = Type.Number(LOCAL),
		MaintenanceCostM = Type.Number(PRIVATE),
		MaintenanceCostA = Type.Number(PRIVATE),
		MaintenanceCostO = Type.Number(PRIVATE),
	},
	
	methods =
	{
		Add = function (self, object)
			super.methods.Add(self, object)
			self:adjust_fleet_totals()
		end,
		
		Sub = function (self, object)
			super.methods.Sub(self, object)
			self:adjust_fleet_totals()
		end,
			
		adjust_fleet_totals = function (self)
			local j = 0
			local mass = 0
			local mm = 0
			local ma = 0
			local mo = 0
			
			for ship in self.Contents.Iterate() do
				if (ship.Class == "SJumpship") then
					j = j + 1
				end
				
				mass = mass + ship.Mass
				mm = mm + ship.MaintenanceCostM
				ma = ma + ship.MaintenanceCostA
				mo = mo + ship.MaintenanceCostO
			end
			
			self.JumpshipCount = j
			self.Mass = mass
			self.MaintenanceCostM = mm
			self.MaintenanceCostA = ma
			self.MaintenanceCostO = mo
		end,
			
		jump = function (self)
			
		end,
		
		on_emerge_from_jump = function (self)
			Log.G("emerging from jump!")
		end,
	}
}
