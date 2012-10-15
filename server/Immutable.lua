local pairs = pairs
local ipairs = ipairs
local error = error
local Utils = require("Utils")

local function Array(data)
	local mt
	
	mt =
	{
		__newindex = function ()
			error("attempt to assign to IArray")
		end,
		
		__index = mt,
		
		__add = function (self, ...)
			local d = {}
			for k, v in ipairs(data) do
				d[k] = v
			end
			
			for _, v in ipairs({...}) do
				d[#d+1] = v
			end
			
			return Array(d)
		end
	}
	
	return setmetatable(data, mt)
end

local function Set(data)
	if data[1] then
		-- The data contains an array part; convert to set format.
		for _, v in ipairs(data) do
			if data[v] then
				Utils.FatalError("set contains duplicate entries")
			end
			data[v] = true
		end
	else
		-- The data contains a set part only; convert to array format.
		local newd = {}
		for k, v in pairs(data) do
			if (type(k) ~= "table") then
				Utils.FatalError("set contains non-table items (is it an array with holes?)")
			end
			newd[k] = true
			newd[#newd+1] = k
		end
		data = newd
	end
	
	local mt
	
	mt =
	{
		__newindex = function ()
			error("attempt to assign to IArray")
		end,
		
		__index = mt,
		
		__add = function (self, ...)
			local d = {}
			for _, v in ipairs(data) do
				d[v] = true
			end
			
			for _, v in ipairs({...}) do
				d[v] = true
			end
			
			return Set(d)
		end,
		
		__sub = function (self, ...)
			local d = {}
			for _, v in ipairs(data) do
				d[v] = true
			end
			
			for _, v in ipairs({...}) do
				d[v] = nil
			end
			
			return Set(d)
		end
	}
	
	return setmetatable(data, mt)
end

return
{
	Array = Array,
	Set = Set 
}
