#!/usr/bin/wsapi.cgi
require("wsapi.request")
require("wsapi.response")

return function (env)
	local request = wsapi.request.new(env)
	local response = wsapi.response.new()
	response:write("<h1>Hello</h1>")
	for k,v in pairs(request.GET) do
	response:write(k.." = "..tostring(v).."<br/>")
	end
	return response:finish()
end


