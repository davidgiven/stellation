#include "globals.h"
#include "mainloop.h"
#include "Log.h"
#include "Hash.h"
#include "Reader.h"
#include "Writer.h"
#include "Transport.h"
#include <deque>

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

		writer.Write("Goodbyte, world!");
	}
};

void Mainloop(const set<string>& endpoints)
{
	Subtransport t(endpoints);
	t.Mainloop();
}
