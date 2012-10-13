local require = require
local print = print
local unpack = unpack
local pairs = pairs
local tonumber = tonumber
local type = type
local setmetatable = setmetatable
local rawset = rawset
local rawget = rawget
local Utils = require("Utils")

local ObjectType =
{
	marshal = function (datum)
		local v = datum.value
		if (v == nil) then
			return -1
		elseif (type(v) == "number") then
			return v
		else
			return v.Oid
		end 
	end,
	
	unmarshal = function (datum, str)
		datum.value = tonumber(str)
	end,
	
	default = function ()
		return -1
	end
}

local TokenType =
{
	marshal = function (datum)
		return datum.value
	end,
	
	unmarshal = function (datum, str)
		datum.value = str
	end,
	
	default = function ()
		return "<default token>"
	end
}

local ObjectSetType =
{
	marshal = function (datum)
		return "{}"
	end,
	
	unmarshal = function (datum, str)
		datum.value = {}
	end,
	
	default = function ()
		return {}
	end
}

local StringType =
{
	marshal = function (datum)
		return datum.value
	end,
	
	unmarshal = function (datum, str)
		datum.value = str
	end,
	
	default = function ()
		return ""
	end
}

local NumberType =
{
	marshal = function (datum)
		return datum.value
	end,
	
	unmarshal = function (datum, str)
		datum.value = str
	end,
	
	default = function ()
		return 0
	end
}
	
local function typeinstance(type, scope)
	local t = {
		__index = type,
		scope = scope
	}
	
	return setmetatable(t, t)
end

return
{ 
	GLOBAL = "global",
	LOCAL = "local",
	SERVERONLY = "serveronly",
	PRIVATE = "private",

	Object = function (scope)
		return typeinstance(ObjectType, scope)
	end,
	
	ObjectSet = function (scope)
		return typeinstance(ObjectSetType, scope)
	end,
	
	String = function (scope)
		return typeinstance(StringType, scope)
	end,
	
	Token = function (scope)
		return typeinstance(TokenType, scope)
	end,
	
	Number = function (scope)
		return typeinstance(NumberType, scope)
	end
}