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
	name = "SDeployable",
	superclass = super,
	
	statics =
	{
		DeployedMaintenanceCostM = 0,
		DeployedMaintenanceCostA = 0,
		DeployedMaintenanceCostO = 0,		
	},
	
	properties =
	{
		Deployed = Type.Boolean(LOCAL),
		MaintenanceCostM = Type.Number(PRIVATE),
		MaintenanceCostA = Type.Number(PRIVATE),
		MaintenanceCostO = Type.Number(PRIVATE),		
	},
	
	methods =
	{
		Init = super.methods.Init,
		
		Deploy = function(self)
			if self.Deployed then
				return
			end
			
			self.Deployed = true
			self.MaintenanceCostM = self.DeployedMaintenanceCostM
			self.MaintenanceCostA = self.DeployedMaintenanceCostA
			self.MaintenanceCostO = self.DeployedMaintenanceCostO
		end,
		
		Mothball = function(self)
			if not self.Deployed then
				return
			end
			
			self.Deployed = false
			self.MaintenanceCostM = 0
			self.MaintenanceCostA = 0
			self.MaintenanceCostO = 0
		end,
	}
}
