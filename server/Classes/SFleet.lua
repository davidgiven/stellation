local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

return
{
	name = "SFleet",
	superclass = require("Classes.SObject"),
	
	properties =
	{
		JumpshipCount = Type.Number(LOCAL)
	},
	
	methods =
	{
	}
}
