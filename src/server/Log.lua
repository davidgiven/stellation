local Socket = require("socket")
local Utils = require("Utils")

local starttime = Socket.gettime()

local function log(tag, msg)
	local t = Socket.gettime() - starttime
	io.stderr:write("[", string.format("%.3f", t), "]: ", tag, ": ", msg, "\n")
end

return
{
	M = function(...) return log("main", Utils.Stringify(...)) end,
	S = function(...) return log("socket", Utils.Stringify(...)) end,
	D = function(...) return log("database", Utils.Stringify(...)) end,
	C = function(...) return log("command", Utils.Stringify(...)) end,
	G = function(...) return log("game", Utils.Stringify(...)) end,
	X = function(...) return log("client", Utils.Stringify(...)) end,
	P = function(...) return log("player", Utils.Stringify(...)) end 
}
