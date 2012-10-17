#!/usr/bin/env luajit-2.0.0-beta9

-- Add the directory containing this script to the package path.

ServerDir = arg[0]:gsub("[^/]+$", "")
package.path = ServerDir .. "?.lua;" .. ServerDir .. "?/init.lua;" .. package.path

local Log = require("Log")
local Utils = require("Utils")
local IO = require("IO")

local socket_filename

do
	local function do_unrecognised(o)
		io.stderr:write("Error: unrecognised option ", o, " (try --help)\n")
		os.exit(1)
	end
	
	local function do_socket(arg)
		socket_filename = arg
		return 1
	end
	
	local function do_help(arg)
		io.stdout:write("stellationclient <options>... [command]\n")
		io.stdout:write("Options:\n")
		io.stdout:write("  -h  --help         Shows this message\n")
		io.stdout:write("  -sX --socket X     Use X as the communications socket\n")
		os.exit(0)
	end
	
	local cbtab =
	{
		["socket"] = do_socket,
		["s"] = do_socket,
		["help"] = do_help,
		["h"] = do_help,
		[" unrecognised"] = do_unrecognised,
		[" filename"] = do_unrecognised 
	}
	
	Utils.ParseCommandLine(arg, cbtab)
	
	if not socket_filename then
		io.stderr:write("Error: you must specify a socket filename\n")
		os.exit(1)
	end
end

Log.M("start")
IO.Connect(socket_filename)
print(IO.SendMsg({1,2,3}))
