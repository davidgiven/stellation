#!/usr/bin/env luajit

-- Add the directory containing this script to the package path.

ServerDir = arg[0]:gsub("[^/]+$", "")
package.path = ServerDir .. "?.lua;" .. ServerDir .. "?/init.lua;" .. package.path

local Log = require("Log")
local Utils = require("Utils")
local IO = require("IO")

local socket_filename

-----------------------------------------------------------------------------
--                                   RPC                                   --
-----------------------------------------------------------------------------

local function RPC(packet)
	return IO.ClientMessage(socket_filename, packet)
end

-----------------------------------------------------------------------------
--                            DATABASE STORAGE                             --
-----------------------------------------------------------------------------

local database = {}
local statics
local properties
local server_time = 0

local function object(oid)
	local o = database[oid]
	if not o then
		o = {}
		database[oid] = o
	end
	return o
end

local typemap = 
{
	string = function (s)
		return s
	end,
	
	number = function (s)
		return tonumber(s)
	end,
	
	object = function (s)
		local oid = tonumber(s)
		return object(oid)
	end,
	
	objectset = function (t)
		local out = {}
		for _, oid in ipairs(t) do
			local o = object(tonumber(oid))
			out[o] = true
		end
		return out
	end
}
 
local function getstatics()
	local r = RPC { cmd = "GetStatics" }
	Utils.Assert(r.result == "OK", "GetStatics failed")
	statics = r.classes
	
	properties = {}
	for _, c in pairs(statics) do
		for name, type in pairs(c.properties) do
			local f = typemap[type.type]
			Utils.Assert(f, "invalid type ", type.type)
			properties[name] = f
		end
	end
end

local function synchronise(msg)
	for oid, v in pairs(msg.changed) do
		oid = tonumber(oid)
		local o = object(oid)
		
		for name, value in pairs(v) do
			o[name] = properties[name](value)
		end
	end
end

-----------------------------------------------------------------------------
--                            ARGUMENT PARSING                             --
-----------------------------------------------------------------------------

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

Log.M("start")
parse_arguments(arg)

local r = RPC {
	cmd = "Authenticate",
	email = "test@invalid.com",
	password = "password"
}
if (r.result ~= "OK") then
	r = RPC {
		cmd = "CreatePlayer",
		email = "test@invalid.com",
		password = "password",
		name = "Test Player",
		empire = "Testing Empire"
	}
	if (r.result ~= "OK") then
		Utils.FatalError("unable to create new test player: ", r.result)
	end
	
	r = RPC {
		cmd = "Authenticate",
		email = "test@invalid.com",
		password = "password"
	}
	if (r.result ~= "OK") then
		Utils.FatalError("unable to authenticate newly created player --- very strange")
	end
end
local cookie = r.cookie
local playeroid = r.oid
Log.X("authentication cookie: ", cookie)

getstatics()

r = RPC {
	cmd = "GameCommand",
	gcmd = "Ping",
	cookie = cookie,
	time = server_time
}
synchronise(r)

local player = object(playeroid)
print(player.Name)
