#!/usr/bin/env luajit

-- Add the directory containing this script to the package path.

ServerDir = arg[0]:gsub("[^/]+$", "")
package.path = ServerDir .. "?.lua;" .. ServerDir .. "?/init.lua;" .. package.path

local Datastore = require "Datastore"
local Properties = require "Properties"
local WorldCreation = require "WorldCreation"
local Log = require("Log")
local Utils = require("Utils")
local IO = require("IO")
local Commands = require("Commands")
local G = require("G")

local database_filename
local socket_filename

-- Parses command line arguments

local function parse_arguments(arg)
	local function do_unrecognised(o)
		io.stderr:write("Error: unrecognised option ", o, " (try --help)\n")
		os.exit(1)
	end
	
	local function do_database(arg)
		database_filename = arg
		return 1
	end
	
	local function do_socket(arg)
		socket_filename = arg
		return 1
	end
	
	local function do_help(arg)
		io.stdout:write("stellationserver <options>...\n")
		io.stdout:write("Options:\n")
		io.stdout:write("  -h  --help         Shows this message\n")
		io.stdout:write("  -dX --database X   Use X as the storage database\n")
		io.stdout:write("  -sX --socket X     Use X as the communications socket\n")
		os.exit(0)
	end
	
	local cbtab =
	{
		["database"] = do_database,
		["d"] = do_database,
		["socket"] = do_socket,
		["s"] = do_socket,
		["help"] = do_help,
		["h"] = do_help,
		[" unrecognised"] = do_unrecognised,
		[" filename"] = do_unrecognised 
	}
	
	Utils.ParseCommandLine(arg, cbtab)
	
	if not database_filename then
		io.stderr:write("Error: you must specify a database filename\n")
		os.exit(1)
	end
	
	if not socket_filename then
		io.stderr:write("Error: you must specify a socket filename\n")
		os.exit(1)
	end
end

-- Process a single incoming message

local function handle_message_cb(msg)
	Log.M("received message ", msg.cmd)
	
	local command = Commands[msg.cmd]
	if not command then
		return
		{
			tag = msg.tag,
			time = G.CanonicalTime,
			result = "BadCommand"
		}
	end
	
	G.CanonicalTime = G.CanonicalTime + 1
	Datastore.Begin()
	local reply = command(msg)
	if (reply.result == "OK") then
		Datastore.Commit()
	else
		Datastore.Rollback()
	end
	
	reply.tag = msg.tag
	reply.time = G.CanonicalTime
	return reply
end

Log.M("start")
parse_arguments(arg)
Datastore.Connect(database_filename)
Properties.Load()

IO.Listen(socket_filename)

G.CanonicalTime = Datastore.CalculateServerCanonicalTime() or 1
Log.M("server canonical time is ", G.CanonicalTime)

Datastore.Begin()

if not Datastore.DoesObjectExist(0) then
	Log.M("initialising new database")
	local SUniverse = Datastore.CreateWithOid(0, "SUniverse")
	local SGalaxy = Datastore.Create("SGalaxy")
	
	SUniverse.Galaxy = SGalaxy
	WorldCreation.InitialiseGalaxy(SGalaxy)
end

Datastore.Commit()

IO.EventLoop(handle_message_cb)

Datastore.Disconnect()
