local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE
local Log = require("Log")

local super = require("Classes.SObject")

return
{
	name = "SFleet",
	superclass = super,
	
	statics =
	{
	},
	
	properties =
	{
		JumpshipCount = Type.Number(LOCAL)
	},
	
	methods =
	{
		Add = function (self, object)
			super.methods.Add(self, object)
			if (object.Class == "SJumpship") then
				self.JumpshipCount = self.JumpshipCount + 1
				Log.G("jumpship count of fleet ", self.Oid, " adjusted to ", self.JumpshipCount)
			end
		end
	}
}
