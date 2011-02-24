#include "globals.h"
#include "mainloop.h"
#include "Log.h"
#include <zmq.hpp>

void Mainloop(const set<string>& zmqspec)
{
	Log() << "initialising network connections";

	zmq::context_t zmqcontext(1);
	zmq::socket_t zmqsocket(zmqcontext, ZMQ_REP);

	for (set<string>::const_iterator i = zmqspec.begin(),
			e = zmqspec.end(); i != e; i++)
	{
		const string& endpoint = *i;

		try
		{
			zmqsocket.bind(endpoint.c_str());
		}
		catch (const zmq::error_t& e)
		{
			Error() << "unable to bind to endpoint '" << endpoint << "': "
					<< e.what();
		}
	}

	Log() << "running...";
	for (;;)
	{
		zmq::message_t message;
		Log() << "waiting";
		zmqsocket.recv(&message);
		Log() << "got message";
		zmqsocket.send(message, 0);
	}
}
