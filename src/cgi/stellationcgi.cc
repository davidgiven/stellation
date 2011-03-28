#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <zmq.hpp>
#include <syslog.h>

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

string safegetenv(const string& name)
{
	const char* result = getenv(name.c_str());
	if (!result)
		throw MalformedRequest;
	return result;
}

int main(int argc, const char* argv[])
{
	openlog("stellationcgi", 0, LOG_USER);
	syslog(LOG_INFO, "started");

	try
	{
		std::stringstream response;

		response << "Content-Type: text/plain\r\n"
					 "Access-Control-Allow-Origin: *\r\n"
					 "\r\n";

		int contentlength = atoi(safegetenv("CONTENT_LENGTH").c_str());
		if (!contentlength)
			throw "No contentlength";

		if (safegetenv("REQUEST_METHOD") != "POST")
			throw "Invalid request method";

		std::istream& data = std::cin;

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
		response << "\r\n";

		/* Done. Dump the result to stdout. */

		std::cout << response.str();
	}
	catch (const char* s)
	{
		syslog(LOG_ERR, "error: %s", s);
		std::cout << "Status: 406 " << s <<
				"\r\n\r\n" << s;
	}
	catch (zmq::error_t& e)
	{
		syslog(LOG_ERR, "zmq error: %s", e.what());
		std::cout << "Status: 500 " << e.what() <<
				"\r\n\r\n" << e.what();
	}
	syslog(LOG_INFO, "ended");

	return 0;
}
