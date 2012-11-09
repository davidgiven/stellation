local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

return
{
	name = "SStar",
	superclass = require("Classes.SObject"),
	
	statics =
	{
		ResourcesA = 0,
		ResourcesO = 0,
		ResourcesM = 0
	},
	
	properties =
	{
		X = Type.Number(GLOBAL),
		Y = Type.Number(GLOBAL),
		Brightness = Type.Number(GLOBAL),
		ResourcesA = Type.Number(LOCAL),
		ResourcesO = Type.Number(LOCAL),
		ResourcesM = Type.Number(LOCAL),
		AsteroidsC = Type.Number(LOCAL),
		AsteroidsM = Type.Number(LOCAL)
	},
	
	methods =
	{
	}
}
