local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

local super = require("Classes.SUnit")

return
{
	name = "SShip",
	superclass = super,
	
	statics =
	{
	},
	
	properties =
	{
	},
	
	methods =
	{
		Init = super.methods.Init,
		Add = super.methods.Add,
		Sub = super.methods.Sub,
	}
}
