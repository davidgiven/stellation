local Tokens = require("Tokens")
local Utils = require("Utils")

local classes, mt

local mt =
{
	__index = function (self, key)
		if (key == "properties") then
			local allproperties = {}
			for _, c in pairs(classes) do
				for p, t in pairs(c.properties) do
					local token = Tokens[p]
					local oldt = allproperties[token]
					if oldt and (oldt ~= t) then
						Utils.FatalError("property type collision for ", p, " on class ", c.name)
					end
					allproperties[token] = t
				end
			end
			
			classes.properties = allproperties
			return allproperties
		else
			Utils.FatalError("no such class ", key)
		end
	end
}

classes = 
{
	SCargoship = require("Classes.SCargoship"),
	SFleet = require("Classes.SFleet"),
	SGalaxy = require("Classes.SGalaxy"),
	SJumpship = require("Classes.SJumpship"),
	SObject = require("Classes.SObject"),
	SPlayer = require("Classes.SPlayer"),
	SShip = require("Classes.SShip"),
	SStar = require("Classes.SStar"),
	STug = require("Classes.STug"),
	SUnit = require("Classes.SUnit"),
	SUniverse = require("Classes.SUniverse"),	
}
setmetatable(classes, mt)

return classes
