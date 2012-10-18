local require = require
local Type = require("Type")
local Utils = require("Utils")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

return
{
	name = "SObject",

	statics =
	{
	},
	
	properties =
	{
		Owner = Type.Object(GLOBAL),
		Class = Type.Token(GLOBAL),
		Contents = Type.ObjectSet(LOCAL),
		Location = Type.Object(GLOBAL),
		Name = Type.String(GLOBAL)
	},
	
	methods =
	{
		Init = function (self)
		end,
		
		Create = function (self, class)
			local o = require("Datastore").Create(class)
			o:Init()
			self:Add(o)
			return o
		end,
	
		Add = function (self, object)
			Utils.Assert(object.Location == nil, "object already belong to something")
			object.Location = self
			self.Contents = self.Contents + object
		end
	}
}
