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
	name = "SFixed",
	superclass = super,
	
	statics =
	{
	},
	
	properties =
	{
	},
	
	methods =
	{
		Init = super.methods.Init
	}
}
