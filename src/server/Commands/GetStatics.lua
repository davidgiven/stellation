local Datastore = require("Datastore")
local Database = require("Database")
local Log = require("Log")
local WorldCreation = require("WorldCreation")
local SQL = Database.SQL
local Classes = require("Classes")

return function (msg)
	local classes = {}
	for _, class in pairs(Classes) do
		local cout = {
			properties = {},
			statics = {}
		}

		if class.superclass then
			cout.superclass = class.superclass.name
		end
			
		for name, type in pairs(class.properties) do
			cout.properties[name] =
			{
				type = type.jstype,
				scope = type.scope
			}
		end
		
		for name, value in pairs(class.statics) do
			cout.statics[name] = value
		end
		
		classes[class.name] = cout
	end

	return
	{
		result = "OK",
		classes = classes
	}
end
