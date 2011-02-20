local inf = io.open(pm.arg[3])
local outf = io.open(pm.arg[4], "wb")

for s in inf:lines() do
	local cs = string.upper(string.sub(s, 1, 1)) ..
		string.sub(s, 2)
	local hash = "_hash_"..s
	outf:write(
		'public:\n',
		'LazyDatum ', cs, ';\n'
	)
end

os.exit()
