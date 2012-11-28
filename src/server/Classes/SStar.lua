local math_random = math.random
local require = require
local Type = require("Type")
local GLOBAL = Type.GLOBAL
local LOCAL = Type.LOCAL
local SERVERONLY = Type.SERVERONLY
local PRIVATE = Type.PRIVATE
local Database = require("Database")
local SQL = Database.SQL
local Log = require("Log")
local Socket = require("socket")

return
{
	name = "SStar",
	superclass = require("Classes.SObject"),
	
	statics =
	{
		ResourcesA = 0,
		ResourcesO = 0,
		ResourcesM = 0,
		Debris = 0
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
		AsteroidsM = Type.Number(LOCAL),
		Debris = Type.Number(LOCAL)
	},
	
	methods =
	{
		Log = function (self, timestamp, ...)
			local playerset = {}
			for o in self.Contents:Iterate() do
				if o:IsA("SFleet") then
					if (o.JumpshipCount > 0) then
						playerset[o.Owner] = true
					end
				end
			end
			
			Database.Log(self, timestamp, playerset, ...)
		end
	}
}
