local print = print
local Utils = require("Utils")

module("Datum")

function Integer()
	local datum = {}
	return setmetatable({},
		{
			_
	local datum = 
	{
		value = 0,
		marshal = function(self)
			return tostring(self.value)
		end,
		
		unmarshal = function(self, str)
			self.value = tonumber(str)
		end
	}
end