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
		Damage = 0
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
		end
	}
}
