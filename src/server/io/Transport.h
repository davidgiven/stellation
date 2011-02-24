#ifndef TRANSPORT_H
#define TRANSPORT_H

class Reader;
class Writer;
#include <zmq.hpp>

class Transport
{
public:
	Transport(const set<string>& endpoints);
	~Transport();

	void Mainloop();
	virtual void Request(Reader& reader, Writer& writer) = 0;

private:
	zmq::context_t _context;
	zmq::socket_t _socket;
};

#endif
