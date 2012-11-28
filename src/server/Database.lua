local Utils = require("Utils")
local Log = require("Log")
local SQL = require("ljsqlite3")
local Socket = require("socket")

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
	
	SQL = compile,
	
	Log = function(location, timestamp, players, ...)
		local locationoid = nil
		if location then
			locationoid = location.Oid
		end
		
		if not timestamp then
			timestamp = Utils.Time()
		end
		local msg = Utils.Stringify(...)
		
		local playerlist = {}
		for p in pairs(players) do
			playerlist[#playerlist+1] = p.Oid
		end
		
		Log.P(locationoid, "@", string.format("%.3f", timestamp),
			" [", table.concat(playerlist, ", "), "]: ", msg)
		
		compile(
			"INSERT INTO logentries (time, location, entry) VALUES (?, ?, ?)"
			):bind(timestamp, locationoid, msg):step()
			
		local row = tonumber(compile("SELECT last_insert_rowid()"):step()[1])
		
		for p in pairs(players) do
			compile(
				"INSERT INTO visiblelogs (player, log) VALUES (?, ?)"
				):bind(p.Oid, row):step()
		end 
	end
	 
}
