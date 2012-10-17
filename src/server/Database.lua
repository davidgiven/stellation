local require = require
local print = print
local unpack = unpack
local pairs = pairs
local tonumber = tonumber
local type = type
local setmetatable = setmetatable
local rawset = rawset
local rawget = rawget
local require = require
local Utils = require("Utils")
local Log = require("Log")
local SQL = require("ljsqlite3")

local database = nil
local statements = {}

local function compile(sql)
	local stmt = statements[sql]
	if stmt then
		stmt:reset()
		return stmt
	end
	
	stmt = database:prepare(sql)
	statements[sql] = stmt
	return stmt
end

return
{
	Connect = function (filename)
		Log.D("opening '", filename, "'")
		database = SQL.open(filename)
		
		local dbinit = Utils.LoadFile("dbinit.sql")
		database:exec(dbinit)
	end,

	Disconnect = function ()
		if database then
			Log.D("closing database")
			database:close()
			database = nil
		end
	end,

	Begin = function ()
		compile("BEGIN"):step()
	end,

	Commit = function ()
		compile("COMMIT"):step()
	end,

	Rollback = function ()
		compile("ROLLBACK"):step()
	end,

	SQL = compile 
}
