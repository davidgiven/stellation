#!/usr/bin/env luajit-2.0.0-beta9

-- Add the directory containing this script to the package path.

ServerDir = arg[0]:gsub("[^/]+$", "")
package.path = ServerDir .. "?.lua;" .. ServerDir .. "?/init.lua;" .. package.path

local Datastore = require "Datastore"
local Properties = require "Properties"
local WorldCreation = require "WorldCreation"
local Log = require("Log")

Log.M("start")
Datastore.Connect("test.db")
Properties.Load()

Datastore.Begin()

local SUniverse = Datastore.CreateWithOid(0, "SUniverse")
local SGalaxy = Datastore.Create("SGalaxy")

SUniverse.Galaxy = SGalaxy
WorldCreation.InitialiseGalaxy(SGalaxy)

SGalaxy:commit()
SUniverse:commit()

Datastore.Commit()

Datastore.Disconnect()
