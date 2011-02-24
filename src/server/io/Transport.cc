#include "globals.h"
#include "mainloop.h"
#include "Log.h"
#include "Hash.h"
#include "Reader.h"
#include "Writer.h"
#include "Transport.h"
#include <list>

using std::list;

class ZMQReader : public Reader
{
public:
	ZMQReader(zmq::socket_t& s):
		_socket(s)
	{ }

	~ZMQReader()
	{
		Consume();
	}

	void Consume()
	{
		while (!_eof)
			Read();
	}

	void Read()
	{
		assert(!_eof);

		_socket.recv(&_message);

		int64_t v;
		size_t s = sizeof(v);
		_socket.getsockopt(ZMQ_RCVMORE, &v, &s);
		_eof = (bool)!v;
	}

	Hash::Type ReadHash()
	{
		Read();
		return Hash::ValidatedHashFromString(
				(const char*) _message.data(),
				_message.size());
	}

	string ReadString()
	{
		Read();
		return string(
				(const char*) _message.data(),
				_message.size());
	}

	bool IsEOF() const
	{
		return _eof;
	}
private:
	zmq::socket_t& _socket;
	zmq::message_t _message;
	iscalar<bool, false> _eof;
};

class ZMQWriter : public Writer
{
public:
	ZMQWriter(zmq::socket_t& s):
		_socket(s)
	{ }

	~ZMQWriter()
	{
		Produce();
	}

	void Produce()
	{
		if (_pending)
		{
			_socket.send(_message, 0);
			_pending = false;
		}
	}

	void Flush()
	{
		if (_pending)
			_socket.send(_message, ZMQ_SNDMORE);
	}

	void Write(const void* data, size_t size)
	{
		Flush();
		_message.rebuild(size);
		memcpy(_message.data(), data, size);
		_pending = true;
	}

	void Write(const string& s)
	{
		Write(s.data(), s.size());
	}

private:
	zmq::socket_t& _socket;
	zmq::message_t _message;
	iscalar<bool, false> _pending;
};

class MemoryWriter : public Writer
{
public:
	MemoryWriter()
	{}

	void SendTo(Writer& writer)
	{
		for (list<string>::const_iterator i = _data.begin(),
				e = _data.end(); i != e; i++)
		{
			writer.Write(*i);
		}
	}

	void Write(const string& s)
	{
		_data.push_back(s);
	}

private:
	list<string> _data;
};

Transport::Transport(const set<string>& zmqspec):
		_context(1),
		_socket(_context, ZMQ_REP)
{
	Log() << "initialising network transport";

	for (set<string>::const_iterator i = zmqspec.begin(),
			e = zmqspec.end(); i != e; i++)
	{
		const string& endpoint = *i;

		try
		{
			Log() << "listening on: " << endpoint;
			_socket.bind(endpoint.c_str());
		}
		catch (const zmq::error_t& e)
		{
			Error() << "unable to bind to endpoint '" << endpoint << "': "
					<< e.what();
		}
	}
}

Transport::~Transport()
{
	Log() << "shutting down network transport";
}

void Transport::Mainloop()
{
	Log() << "waiting for connections...";
	for (;;)
	{
		MemoryWriter writer;

		/* Remember: can't have a ZMQReader and ZMQWriter instantiated at the
		 * same time.
		 */

		{
			ZMQReader reader(_socket);
			Request(reader, writer);
		}

		{
			ZMQWriter zmqwriter(_socket);
			writer.SendTo(zmqwriter);
		}
	}
}
