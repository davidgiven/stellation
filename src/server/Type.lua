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
local Immutable = require("Immutable")

local ObjectType =
{
	marshal = function (datum)
		local v = datum.value
		if (v == nil) then
			return nil
		else
			return v.Oid
		end 
	end,
	
	unmarshal = function (datum, str)
		local findproxy = require("Datastore").Object
		datum.value = findproxy(tonumber(str))
	end,
	
	default = function ()
		return nil
	end,
	
	sqltype = "INTEGER"
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
	end,
	
	sqltype = "INTEGER"
}

local ObjectSetType =
{
	marshal = function (datum)
		local s = {}
		for _, item in ipairs(datum.value) do
			s[#s+1] = item.Oid
		end
		return table.concat(s, " ")
	end,
	
	unmarshal = function (datum, str)
		local findproxy = require("Datastore").Object
		local t = {}
		
		for s in string.gmatch(str, "%d+") do
			local oid = tonumber(s)
			t[findproxy(oid)] = true
		end
		
		datum.value = Immutable.Set(t)
	end,
	
	default = function ()
		return Immutable.Set({})
	end,
	
	sqltype = "TEXT"
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
	end,
	
	sqltype = "TEXT"
}

local NumberType =
{
	marshal = function (datum)
		return datum.value
	end,
	
	unmarshal = function (datum, str)
		datum.value = tonumber(str)
	end,
	
	default = function ()
		return 0
	end,
	
	sqltype = "REAL"
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