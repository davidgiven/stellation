local inf = io.open(pm.arg[3])
local outf = io.open(pm.arg[4], "wb")

local hashes = {}
local macro = {}

for s in inf:lines() do
	local cs = string.upper(string.sub(s, 1, 1)) ..
		string.sub(s, 2)
	
	table.insert(hashes,
		'static int _hash_'..s..' = DatabaseObject::HashPropertyName("'..s..'");\n'
	)
	
	table.insert(macro,
		cs..'(dbo, _hash_'..s..'), \\\n'
	)
end

outf:write(table.concat(hashes))

outf:write('#define DATUM_INITIALISERS \\\n')
outf:write(table.concat(macro))
outf:write('\n')

os.exit()
