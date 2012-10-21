#!/usr/bin/env luajit

-- Add the directory containing this script to the package path.

ServerDir = arg[0]:gsub("[^/]+$", "")
package.path = ServerDir .. "?.lua;" .. ServerDir .. "?/init.lua;" .. package.path

local Log = require("Log")
local Utils = require("Utils")
local IO = require("IO")

local socket_filename

local function parse_arguments(arg)
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

local function msg(packet)
	return IO.ClientMessage(socket_filename, packet)
end

Log.M("start")
parse_arguments(arg)

local r = msg {
	cmd = "Authenticate",
	email = "test@invalid.com",
	password = "password"
}
if (r.result ~= "OK") then
	r = msg {
		cmd = "CreatePlayer",
		email = "test@invalid.com",
		password = "password",
		name = "Test Player",
		empire = "Testing Empire"
	}
	if (r.result ~= "OK") then
		Utils.FatalError("unable to create new test player: ", r.result)
	end
	
	r = msg {
		cmd = "Authenticate",
		email = "test@invalid.com",
		password = "password"
	}
	if (r.result ~= "OK") then
		Utils.FatalError("unable to authenticate newly created player --- very strange")
	end
end
local cookie = r.cookie
Log.X("authentication cookie: ", cookie)

--r = msg {
--	cmd = "GetStatics"
--}

r = msg {
	cmd = "GameCommand",
	cookie = cookie,
	ctime = 0
}
