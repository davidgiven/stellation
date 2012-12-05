local math_random = math.random
local require = require
local Type = require("Type")
local Utils = require("Utils")
local L = Utils.Unindent
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

local super = require("Classes.SObject")

return
{
	name = "SMessage",
	superclass = super,
	
	statics =
	{
	},
	
	properties =
	{
		Text = Type.String(GLOBAL)
	},
	
	methods =
	{
		Init = super.methods.Init
	}
}
