local Utils = require("Utils")
local Datastore = require("Datastore")

module("Properties")

local tokens = {}

function Load()
	local fp = Utils.OpenFile("properties.dat")

	for line in fp:lines() do
		repeat
			local t = line:match("^%+%s*(%w+)%s*$")
			if t then
				tokens[t] = -1
				break
			end
			
			local t, type, scope = line:match("^(%w+)%s+(%w+)%s+(%w+)%s*$")
			if scope then
				tokens[t] = -1
				break
			end
			
			local class = line:match("^%*%s*(%w+)%s*$")
			if class then
				tokens[class] = -1
				break
			end
			
			local class, superclass = line:match("^%*%s*(%w+)%s+(%w+)%s*$")
			if superclass then
				tokens[class] = -1
				tokens[superclass] = -1
				break
			end
			
			if line:match("^%s*#") then
				-- Comment, ignore
				break
			end
			
			if line:match("^s*$") then
				-- Empty line, ignore
				break
			end
			
			Utils.FatalError("Syntax error in properties.dat, at line: ", line)
		until true	
	end
	
	fp:close()
	
	--Datastore.SetTokens(tokens)
end
