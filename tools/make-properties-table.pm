local string_find = string.find
local string_sub = string.sub
local string_upper = string.upper
local string_len = string.len
local table_remove = table.remove
local table_insert = table.insert

local inf = io.open(pm.arg[3])
local tokensoutf = io.open(pm.arg[4], "wb")
local definitionsoutf = io.open(pm.arg[5], "wb")
local propertyaccessorshoutf = io.open(pm.arg[6], "wb")

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

-- Write out the tokens file.

do
	for token in pairs(tokens) do
		tokensoutf:write(token, '\n')
	end
end

-- Write out the property tables.

do
	for name, data in pairs(properties) do
		definitionsoutf:write(
			'const Property _propdata_', name, ' = ',
			'{Datum::', data.type, ', Property::', data.scope, '};\n') 
			 
	end
	
	definitionsoutf:write(
		'const PropertyDataMap::value_type _propdata[] =\n',
		'{\n')
		
	for name in pairs(properties) do
		definitionsoutf:write(
			'\tPropertyDataMap::value_type(Hash::', cname(name), ', _propdata_', name, '),\n'
		)
	end
	
	definitionsoutf:write(
		'};\n',
		'const PropertyDataMap propertyDataMap(\n',
		'\t_propdata, _propdata+boost::size(_propdata));\n'
	)
end

-- Write out the initialisation tables.

do
	for name in pairs(classes) do
		definitionsoutf:write(
			'extern const ClassInitData _classdata_', name, ';\n'
		)
	end
	
	for name, class in pairs(classes) do
		definitionsoutf:write(
			'const Hash::Type _proplist_', name, '[] =\n',
			'\t{\n')
		for _, property in ipairs(class) do
			definitionsoutf:write('\t\tHash::', cname(property.name), ',\n')
		end
		definitionsoutf:write('};\n')
			
		definitionsoutf:write(
			'const ClassInitData _classdata_', name, ' =\n',
			'\t{\n')
		if class.superclass then
			definitionsoutf:write('\t\t&_classdata_', class.superclass, ',\n')
		else
			definitionsoutf:write('\t\tNULL,\n')
		end
		definitionsoutf:write(
			'\t\tboost::size(_proplist_', name, '),\n',
			'\t\t_proplist_', name, '\n'
		)
		
		definitionsoutf:write('\t};\n')
	end
	
	definitionsoutf:write(
		'const ClassInitialisationMap::value_type _classdata[] = {\n'
	)
	for name, _ in pairs(classes) do
		definitionsoutf:write(
			'\tClassInitialisationMap::value_type(',
			'Hash::', cname(name), ', _classdata_', name, '),\n'
		)
	end
	definitionsoutf:write('};\n')
			
	definitionsoutf:write(
		'const ClassInitialisationMap classInitialisationMap(',
			'_classdata, _classdata+boost::size(_classdata));'
	)
end

-- Write out the property accessors headers.

do
	for name, class in pairs(classes) do
		propertyaccessorshoutf:write(
			'class ', cname(name), 'Properties\n',
			'{\n',
			'public:\n',
			'\t', cname(name), 'Properties(Database::Type oid):'
		)
			
		local first = true
		for _, property in ipairs(class) do
			if not first then
				propertyaccessorshoutf:write(",\n\t\t")
			else
				propertyaccessorshoutf:write("\n\t\t")
				first = false
			end
			
			propertyaccessorshoutf:write(
				cname(property.name),
				"(oid, Hash::", cname(property.name), ")")
		end
		propertyaccessorshoutf:write(
			'\n',
			'\t{}\n'
		)
		
		for _, property in ipairs(class) do
			propertyaccessorshoutf:write('\tLazyDatum ', cname(property.name), ';\n')
		end
		propertyaccessorshoutf:write('};\n')
	end
end

tokensoutf:close()
definitionsoutf:close()
propertyaccessorshoutf:close()

os.exit()