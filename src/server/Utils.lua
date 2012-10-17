local os = os
local io = io
local table = table
local ipairs = ipairs
local tostring = tostring
local unpack = unpack
local error = error
local ServerDir = ServerDir

--- Opens a resource file (but does not read it in).
-- Files are lookup up in the server directory.

local function OpenFile(filename)
	local fp, e = io.open(ServerDir .. filename, "r")
	if (e) then
		FatalError(e)
	end
	
	return fp
end 

--- Loads a resource file.
-- Files are looked up in the server directory.

local function LoadFile(filename)
	local fp = OpenFile(filename)
	local data = fp:read("*a")
	fp:close()
	return data
end 

--- Halts with a fatal error.

local function FatalError(...)
	local args = {...}
	for k, v in ipairs(args) do
		args[k] = tostring(args[k])
	end
	error(table.concat(args))
end

--- Checks the first parameter for an error code; if so, halts with a
--- fatal error.
--
--     e: error code, or nil for success
--     ...: message (error code is appended)

local function Check(e, ...)
	if e then
		local args = {...}
		args[#args+1] = ": "
		args[#args+1] = e
		FatalError(unpack(args))
	end
end

--- Checks the first parameter for false; if so, halts with a fatal error.
--
--     e: any value
--     ...: message (error code is appended)

local function Assert(e, ...)
	if not e then
		local args = {...}
		FatalError(unpack(args))
	end
end

return
{
	OpenFile = OpenFile,
	LoadFile = LoadFile,
	FatalError = FatalError,
	Check = Check,
	Assert = Assert
}
