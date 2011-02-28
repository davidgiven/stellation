#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <cgicc/Cgicc.h>
#include <cgicc/HTTPStatusHeader.h>
#include <zmq.hpp>

using std::string;

static const char* MalformedRequest = "Malformed Request";

static string quote(const string& sin)
{
	std::stringstream sout;
	sout << '"';

	for (int i = 0; i < sin.size(); i++)
	{
		int c = sin[i];
		if (c < 32)
		{
			sout << "\\x"
				<< std::setw(2) << std::setfill('0') << std::right << (int)c;
		}
		else if (c == '"')
			sout << "\\\"";
		else
			sout << (char)c;
	}

	sout << '"';
	return sout.str();
}

int main(int argc, const char* argv[])
{
	cgicc::Cgicc cgi;

	try
	{
		const cgicc::CgiEnvironment& env = cgi.getEnvironment();
		string requesttype = env.getRequestMethod();

		string sdata;
		if (requesttype == "GET")
			sdata = env.getQueryString();
		else if (requesttype == "POST")
			sdata = env.getPostData();
		else
			throw MalformedRequest;

		std::istringstream data(sdata);

		zmq::context_t zmqcontext(1);
		zmq::socket_t zmqsocket(zmqcontext, ZMQ_REQ);
		zmqsocket.connect(SERVER_ENDPOINT);

		/* Send request to server. */

		for (;;)
		{
			char buffer[16*1024];

			data.getline(buffer, sizeof(buffer), '\01');
			zmq::message_t message(strlen(buffer));
			strcpy((char*) message.data(), buffer);

			bool more = !data.eof();
			zmqsocket.send(message, more ? ZMQ_SNDMORE : 0);
			if (!more)
				break;
		}

		/* Read request back from server. */

		std::stringstream response;
		response << "[";
		for (;;)
		{
			zmq::message_t message;
			zmqsocket.recv(&message);

			int64_t more;
			size_t moresz = sizeof(more);
			zmqsocket.getsockopt(ZMQ_RCVMORE, &more, &moresz);

			const string s((const char*)message.data(), message.size());
			response << quote(s);

			if (more)
				response << ", ";
			else
				break;
		}
		response << "]";

		std::cout << cgicc::HTTPStatusHeader(200, "OK")
				<< response.str() << std::endl;
	}
	catch (const char* s)
	{
		std::cout << cgicc::HTTPStatusHeader(406, s) << std::endl;
	}
	catch (zmq::error_t& e)
	{
		std::cout << cgicc::HTTPStatusHeader(500, e.what());
	}

	return 0;
}
