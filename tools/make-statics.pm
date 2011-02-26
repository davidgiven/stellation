local string_find = string.find
local string_gsub = string.gsub
local string_format = string.format
local table_remove = table.remove
local table_insert = table.insert
local table_concat = table.concat

local inf = io.open(pm.arg[3])
local outf = io.open(pm.arg[4], "wb")

local pushed = nil
local function read()
	if pushed then
		local p = pushed
		pushed = nil
		return p
	end
	return inf:read("*l")
end

local function unread(s)
	if pushed then
		error("unread twice!")
	end
	pushed = s
end

local data = {}
local current

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
		
		["^%s*%*%s*(%w+)%s*$"] = function(classname)
			-- class declaration
			
			current = {}
			data[classname] = current
		end,
		
		["^%s*(%w+)%s+([^%s].*)%s*$"] = function(name, value)
			-- inline value
			
			current[name] = value
		end,
		
		["^%s*(%w+)%s*$"] = function(name)
			-- block value
			
			local value = {}
			while true do
				local s = read()
				if not string_find(s, "^%s") then
					unread(s)
					break
				end
				
				table_insert(value, s)
			end
			
			value = table_concat(value, " ")
			value = string_gsub(value, "%s%s+", " ")
			value = string_gsub(value, "^%s*", "")
			value = string_gsub(value, "%s*$", "")
			
			current[name] = value
		end
	}
	
	while true do
		local line = read()
		if not line then
			break
		end
		
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
			error("unable to parse line "..lineno.." of statics file")
		end
		
		table_remove(matchr, 1)
		table_remove(matchr, 1)
		matchf(unpack(matchr))
		
		lineno = lineno + 1
	end
end

outf:write('static const struct { Hash::Type cid; Hash::Type kid; } allstatics[] =\n')
outf:write('{\n') 
local combined = {}
for i1, c in pairs(data) do
	for i2, v in pairs(c) do
		local k = 'PAIR(Hash::'..i1..', Hash::'..i2..')'
		combined[k] = v
		
		outf:write('\t{ Hash::', i1, ', Hash::', i2, ' },\n')
	end
end
outf:write('};\n')

outf:write('double GetNumberStatic(Hash::Type cid, Hash::Type kid)\n')
outf:write('{\n')
outf:write('\tswitch (PAIR(cid, kid))\n')
outf:write('\t{\n')
for k, v in pairs(combined) do
	if tonumber(v) then
		outf:write(string_format("\t\tcase %s: return %s;\n", k, v))
	end
end
outf:write('\t\tdefault: throw Hash::ObjectDoesNotExist;\n')
outf:write('\t}\n')
outf:write('}\n')

outf:write('string GetStringStatic(Hash::Type cid, Hash::Type kid)\n')
outf:write('{\n')
outf:write('\tswitch (PAIR(cid, kid))\n')
outf:write('\t{\n')
for k, v in pairs(combined) do
	outf:write(string_format("\t\tcase %s: return %q;\n", k, v))
end
outf:write('\t\tdefault: throw Hash::ObjectDoesNotExist;\n')
outf:write('\t}\n')
outf:write('}\n')

os.exit(0)
