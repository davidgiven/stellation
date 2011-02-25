#include "globals.h"
#include "mainloop.h"
#include "Log.h"
#include "Hash.h"
#include "Reader.h"
#include "Writer.h"
#include "Transport.h"
#include <deque>

static void CreatePlayer(Reader& reader, Writer& writer)
{
	map<Hash::Type, string> data;

	while (!reader.IsEOF())
	{
		Hash::Type k = reader.ReadHash();
		string v = reader.ReadString();
		data[k] = v;
	}

	Log() << "Request to create player "
			<< data[Hash::PlayerName] << " of " << data[Hash::EmpireName];
}

class Subtransport : public Transport
{
public:
	Subtransport(const set<string>& endpoints):
		Transport(endpoints)
	{}

	void Request(Reader& reader, Writer& writer)
	{
		Hash::Type command = reader.ReadHash();
		Log() << "received message " << command;

		switch (command)
		{
			case Hash::CreatePlayer:
				CreatePlayer(reader, writer);
				break;

			default:
				Log() << "malformed message!";
		}

		writer.Write("Goodbyte, world!");
	}
};

void Mainloop(const set<string>& endpoints)
{
	Subtransport t(endpoints);
	t.Mainloop();
}
