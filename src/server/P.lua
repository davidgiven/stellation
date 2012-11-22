local Database = require("Database")
local SQL = Database.SQL

local object = {}
setmetatable(object,
	{
		__index = function(self, key)
			local query = SQL(
				"SELECT value FROM variables WHERE key = ?"
				):bind(key):step()
			if not query then
				return nil
			end
			return query[1]
		end,
		
		__newindex = function(self, key, value)
			SQL("INSERT OR REPLACE INTO variables (key, value) VALUES (?, ?)"
				):bind(key, value):step()
		end
	}
)

return object
