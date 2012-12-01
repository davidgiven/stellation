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
	name = "SUnit",
	superclass = super,
	
	statics =
	{
		Damage = 0,
		MaintenanceCostM = 0,
		MaintenanceCostA = 0,
		MaintenanceCostO = 0,
	},
	
	properties =
	{
		Mass = Type.Number(LOCAL),
		Damage = Type.Number(LOCAL),
	},
	
	methods =
	{
		Init = function (self)
			super.methods.Init(self)
			self.Mass = self.RestMass
		end,
		
		Add = super.methods.Add,
		Sub = super.methods.Sub,

		Starve = function (self, timestamp)
			self.Location:Log(timestamp, "unit ", self.Name, " has starved.")
		end,
		
		FindStar = function (self)
			local loc = self.Location
			while loc and not loc:IsA("SStar") do
				loc = loc.Location
			end
			return loc
		end,
		
		FindFleet = function (self)
			local loc = self.Location
			while loc and not loc:IsA("SFleet") do
				loc = loc.Location
			end
			return loc
		end,
		
		AdjustFleetTotals = function (self)
			self:FindFleet():AdjustFleetTotals()
		end,
			
		Scrap = function (self)
			local star = self:FindStar() 
			star.Debris = star.Debris + self.Mass
			self:Destroy()
		end,
	}
}
