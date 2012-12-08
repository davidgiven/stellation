local math_random = math.random
local require = require
local Type = require("Type")
local Utils = require("Utils")
local L = Utils.Unindent
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

local super = require("Classes.SDeployable")

return
{
	name = "SRefinery",
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
