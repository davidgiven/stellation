local Utils = require("Utils")
local Log = require("Log")
local UnixSocket = require("socket.unix")
local JSON = require("cjson")
local Posix = require("posix")

JSON.encode_sparse_array(true)

local socket

local function sendall(s, data)
	local i = 1
	while (i < data:len()) do
		local ii, e = s:send(data, i)
		Utils.Check(e, "failure during socket send")
		i = ii + 1
	end
end

local function safe_decode(json)
	local e, data = pcall(
		function()
			return JSON.decode(json)
		end
	)
	
	if e then
		return data
	else
		return nil
	end
end

return
{
	Listen = function (filename)
		socket = UnixSocket()
		os.remove(filename)
		local _, e = socket:bind(filename)
		Utils.Check(e, "unable to bind socket")
		socket:listen()
		Log.S("listening on ", filename)
		
		_, e = Posix.chmod(filename, "a+rw")
	end,
	
	EventLoop = function(callback)
		while true do
			Log.S("waiting for connection")
			local slave = socket:accept()
			Log.S("received: ", slave)
			
			local recvs = slave:receive("*l")

			--Log.S("< ", recvs)
			local recv = safe_decode(recvs)
			local replys
			if recv then
				local reply = callback(recv)
				replys = JSON.encode(reply)
			else
				replys = '{"result":"MalformedCommand"}'
			end
				
			Log.S("> ", replys)
				
			sendall(slave, replys .. "\n")
			slave:close()
		end
	end,
	
	Connect = function (filename)
		Log.S("connected to ", filename)
	end,
	
	ClientMessage = function (filename, xmsg)
		local socket = UnixSocket()
		local _, e = socket:connect(filename)
		Utils.Check(e, "unable to connect socket")

		local xmsgs = JSON.encode(xmsg) .. "\n"
		sendall(socket, xmsgs)
		local recvs, e = socket:receive("*l")
		Utils.Check(e, "error on socket recv")
		socket:close()
		
		return JSON.decode(recvs)
	end,
}
