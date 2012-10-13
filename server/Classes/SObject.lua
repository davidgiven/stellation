local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE

return
{
	name = "SObject",

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
	}
}
