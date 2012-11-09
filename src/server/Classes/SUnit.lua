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
	},
	
	properties =
	{
		Mass = Type.Number(LOCAL),
		Damage = Type.Number(LOCAL),
	},
	
	methods =
	{
		Init = function (self)
			self.Damage = 0
			
			super.methods.Init(self)
		end
	}
}
