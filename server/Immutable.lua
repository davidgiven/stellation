local pairs = pairs
local ipairs = ipairs
local error = error

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
	local mt
	
	mt =
	{
		__newindex = function ()
			error("attempt to assign to IArray")
		end,
		
		__index = mt,
		
		__add = function (self, ...)
			local d = {}
			for k in pairs(data) do
				d[k] = true
			end
			
			for _, v in ipairs({...}) do
				d[v] = true
			end
			
			return Set(d)
		end,
		
		__sub = function (self, ...)
			local d = {}
			for k in pairs(data) do
				d[k] = true
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
