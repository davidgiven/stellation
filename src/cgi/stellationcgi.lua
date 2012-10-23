#!/usr/bin/wsapi.cgi
require("wsapi.request")
require("wsapi.response")
local UnixSocket = require("socket.unix")

local SOCKETPATH = "/tmp/stellation.socket"

local function sendall(s, data)
	local i = 1
	while (i < data:len()) do
		local ii, e = s:send(data, i)
		if e then
			return nil, e
		end
		i = ii + 1
	end
end

return function (env)
	local request = wsapi.request.new(env)
	local response = wsapi.response.new()
	
	response:content_type("application/json")
	local indata = request.POST.data
	if not indata then
		response.status = 400
		return response:finish()
	end

	local socket = UnixSocket()
	local _, e = socket:connect(SOCKETPATH)
	if e then
		response.status = 503
		response:write(e)
		return response:finish()
	end
	
	local _, e = sendall(socket, indata .. "\n")
	if e then
		response.status = 503
		response:write(e)
		return response:finish()
	end
	
	local outdata, e = socket:receive("*l")
	if e then
		response.status = 500
		response:write(e)
		return response:finish()
	end
	
	socket:close()
		
	response:write(outdata)
	return response:finish()
end


