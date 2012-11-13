local Utils = require("Utils")
local Log = require("Log")
local SQL = require("ljsqlite3")

local database = nil
local statements = {}
local rollbacknotifications = {}

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

local dblocked = false

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
		Utils.Assert(not dblocked, "BEGIN inside transaction")
		Log.D("BEGIN")
		compile("BEGIN"):step()
		dblocked = true
	end,

	Commit = function ()
		Utils.Assert(dblocked, "COMMIT outside transaction")
		Log.D("COMMIT")
		compile("COMMIT"):step()
		dblocked = false
	end,

	Rollback = function ()
		Utils.Assert(dblocked, "ROLLBACK outside transaction")
		Log.D("ROLLBACK")
		compile("ROLLBACK"):step()
		dblocked = false
		
		for k in pairs(rollbacknotifications) do
			k()
		end		
	end,

	AddRollbackNotification = function (f)
		rollbacknotifications[f] = true
	end,
	
	SQL = compile 
}
