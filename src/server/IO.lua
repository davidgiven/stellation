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
local UnixSocket = require("socket.unix")
local JSON = require("cjson")

local socket

local function sendall(s, data)
	local i = 1
	while (i < data:len()) do
		local ii, e = s:send(data, i)
		Utils.Check(e, "failure during socket send")
		i = ii + 1
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
	end,
	
	EventLoop = function(callback)
		while true do
			Log.S("waiting for connection")
			local slave = socket:accept()
			Log.S("received: ", slave)
			
			local recvs = slave:receive("*l")
			Log.S("rcv: ", recvs)
			
			local reply = JSON.encode("ack") .. "\n"
			sendall(slave, reply)
			
			slave:close()
		end
	end,
	
	Connect = function (filename)
		socket = UnixSocket()
		local _, e = socket:connect(filename)
		Utils.Check(e, "unable to connect socket")
		Log.S("connected to ", filename)
	end,
	
	SendMsg = function (xmsg)
		local xmsgs = JSON.encode(xmsg) .. "\n"
		sendall(socket, xmsgs)
		local recvs, e = socket:receive("*l")
		Utils.Check(e, "error on socket recv")
		return JSON.decode(recvs)
	end,
}
