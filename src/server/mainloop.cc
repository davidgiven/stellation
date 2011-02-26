#include "globals.h"
#include "mainloop.h"
#include "Log.h"
#include "Hash.h"
#include "Reader.h"
#include "Writer.h"
#include "Transport.h"
#include "SUniverse.h"
#include "SPlayer.h"
#include "fileio.h"
#include "worldcreation.h"
#include <deque>

static void CreatePlayer(Reader& reader)
{
	string playername;
	string empirename;
	string email;
	string password;

	while (!reader.IsEOF())
	{
		Hash::Type key = reader.ReadHash();
		string value = reader.ReadString();

		switch (key)
		{
			case Hash::PlayerName:     playername = value; break;
			case Hash::EmpireName:     empirename = value; break;
			case Hash::Email:          email = value; break;
			case Hash::Password:       password = value; break;

			default:
				throw Hash::MalformedCommand;
		}
	}

	CreatePlayer(playername, empirename, email, password);
}

static string Authenticate(Reader& reader)
{
	string playername = reader.ReadString();
	if (!reader.IsEOF())
		throw Hash::MalformedCommand;

	SUniverse universe(Database::Universe);
	Database::Type playeroid = universe.Players.FetchFromMap(playername);

	Log() << "authenticating " << playername << " (who is " << playeroid << ")";

	return "fnord";
}

class Subtransport : public Transport
{
public:
	Subtransport(const set<string>& endpoints):
		Transport(endpoints)
	{}

	void Request(Reader& reader, Writer& writer)
	{
		int tag = reader.ReadNumber();
		Hash::Type command = reader.ReadHash();
		Log() << "received message tag " << tag << " command: " << command;

		Hash::Type resultcode;
		try
		{
			switch (command)
			{
				case Hash::CreatePlayer:
					CreatePlayer(reader);
					writer.Write(tag);
					writer.Write(Hash::OK);
					break;

				case Hash::Authenticate:
				{
					string s = Authenticate(reader);
					writer.Write(tag);
					writer.Write(Hash::OK);
					writer.Write(s);
					break;
				}

				default:
					throw Hash::MalformedCommand;
			}

			DatabaseCommit();
		}
		catch (Hash::Type error)
		{
			Log() << "command threw error: " << error;

			DatabaseRollback();
			writer.Write(tag);
			writer.Write(error);
		}
	}
};

void Mainloop(const set<string>& endpoints)
{
	Subtransport t(endpoints);
	t.Mainloop();
}
