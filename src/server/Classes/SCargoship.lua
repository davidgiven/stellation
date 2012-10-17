local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

local super = require("Classes.SShip")

return
{
	name = "SCargoship",
	superclass = super,
	
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
