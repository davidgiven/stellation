local os = os
local io = io
local table = table
local ipairs = ipairs
local tostring = tostring
local unpack = unpack
local error = error
local Socket = require("socket")
local gettime = Socket.gettime

local starttime = gettime()

local function log(tag, args)
	for k, v in ipairs(args) do
		args[k] = tostring(args[k])
	end
	local t = gettime() - starttime
	io.stderr:write("[", string.format("%.3f", t), "]: ", tag, ": ",
		table.concat(args), "\n")
end

return
{
	M = function(...) return log("main", {...}) end,
	S = function(...) return log("socket", {...}) end,
	D = function(...) return log("database", {...}) end,
	C = function(...) return log("command", {...}) end,
	G = function(...) return log("game", {...}) end,
	X = function(...) return log("client", {...}) end,
}
