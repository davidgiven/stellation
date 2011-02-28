local string_find = string.find
local string_sub = string.sub
local string_upper = string.upper
local string_len = string.len
local table_remove = table.remove
local table_insert = table.insert

local inf = io.open(pm.arg[3])
local sbaseoutf = io.open(pm.arg[4], "wb")
local hashoutf = io.open(pm.arg[5], "wb")

local function cname(n)
	return string_upper(string_sub(n, 1, 1))..string_sub(n, 2)
end

local tokens = {}
local properties = {}
local classes = {}

-- Parse the input file.

do
	local currentclass
	local lineno = 1
	
	local lineparser =
	{
		["^%s*$"] = function()
			-- empty line, do nothing
		end,
		
		["^%s*#"] = function()
			-- line with comment, do nothing
		end,
		
		["^%+%s*(%w+)%s*$"] = function(token)
			-- just add this token to the table; not a property
			tokens[token] = true
		end,
		
		["^%s*%*%s*(%w+)%s*$"] = function(classname)
			-- root class declaration
			tokens[classname] = true
			
			if classes[classname] then
				error(classname.." has already been defined as a class at line "..lineno)
			end
			
			currentclass = {}
			classes[classname] = currentclass
		end,
		
		["^%s*%*%s*(%w+)%s*(%w+)%s*$"] = function(classname, superclassname)
			-- derived class declaration
			tokens[classname] = true
			
			if classes[classname] then
				error(classname.." has already been defined as a class at line "..lineno)
			end
			
			if not classes[superclassname] then
				error(superclassname.." has not been defined as a class at line "..lineno)
			end
			
			currentclass = {superclass=superclassname}
			classes[classname] = currentclass
		end,
		
		["^%s*(%w+)%s*(%w+)%s*(%w+)%s*$"] = function(name, type, scope)
			-- property definition
			tokens[name] = true
			
			if not currentclass then
				error("property definition without class declaration at line "..lineno)
			end
			
			if properties[name] then
				error("a property called "..name.." has already been defined, at line "..lineno)
			end
			
			local data = {name=name, type=type, scope=scope}
			properties[name] = data
			table_insert(currentclass, data)
		end
	}
	
	for line in inf:lines() do
		local matchr, matchf
		for pattern, func in pairs(lineparser) do
			local r = {string_find(line, pattern)}
			if r[1] then
				matchr = r
				matchf = func
				break
			end
		end
		
		if not matchr then
			error("unable to parse line "..lineno.." of properties file")
		end
		
		table_remove(matchr, 1)
		table_remove(matchr, 1)
		matchf(unpack(matchr))
		
		lineno = lineno + 1
	end
end

-- Write out the property tables.

do
	sbaseoutf:write(
		'package com.cowlark.stellation3.client.database;\n',
		'import com.cowlark.stellation3.client.database.values.*;\n',
		'import java.util.HashMap;\n',
		'public class SBase\n',
		'{\n')
		
	for name, data in pairs(properties) do
		sbaseoutf:write(
			'\tpublic ', data.type, ' ', name, ' = new ', data.type, '();\n'
		)
	end
		
	sbaseoutf:write(
		'\n',
		'\tpublic DATUM getDatumByName(Hash h)\n',
		'\t{\n',
		'\t\tswitch (h)\n',
		'\t\t{\n'
	)
	
	for name, data in pairs(properties) do
		sbaseoutf:write(
			'\t\t\tcase ', name, ': return ', name, ';\n'
		)
	end
	
	sbaseoutf:write(
		'\t\t\tdefault: return null;\n',
		'\t\t}\n',
		'\t}\n',
		'}\n'
	)
end

-- Write out the hash enumeration.

do
	hashoutf:write(
		'package com.cowlark.stellation3.client.database;\n',
		'public enum Hash\n',
		'{\n')
		
	for token in pairs(tokens) do
		hashoutf:write('\t', token, ',\n')
	end
	
	hashoutf:write(
		'}\n'
	)
end

sbaseoutf:close()
hashoutf:close()

os.exit()