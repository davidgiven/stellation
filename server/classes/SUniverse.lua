local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

return
{
	name = "SUniverse",
	superclass = require("classes.SObject"),

	properties =
	{
		Galaxy = Type.Object(GLOBAL)
	},
	
	methods =
	{
	}
}