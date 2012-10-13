#!/usr/bin/env luajit-2.0.0-beta9

-- Add the directory containing this script to the package path.

ServerDir = arg[0]:gsub("[^/]+$", "")
package.path = ServerDir .. "?.lua;" .. ServerDir .. "?/init.lua;" .. package.path

local Datastore = require "Datastore"
local Properties = require "Properties"

Datastore.Connect("test.db")
Datastore.Init()
Datastore.Open()
Properties.Load()

Datastore.Begin()

local SUniverse = Datastore.CreateWithOid(0, "SUniverse")
local SGalaxy = Datastore.Create("SGalaxy")

SUniverse.Galaxy = SGalaxy

for i = 1, 10 do
	local s = Datastore.Create("SStar")
	s.X = math.random() * 10 - 5 
	s.Y = math.random() * 10 - 5 
	s:commit()
end

SGalaxy:commit()
SUniverse:commit()

Datastore.Commit()

Datastore.Disconnect()
