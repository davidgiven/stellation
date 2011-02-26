#include "globals.h"
#include "mainloop.h"
#include "Log.h"
#include "Hash.h"
#include "Reader.h"
#include "Writer.h"
#include "Transport.h"
#include "SPlayer.h"
#include "fileio.h"
#include "worldcreation.h"
#include "auth.h"
#include "engine.h"
#include "utils.h"
#include <deque>

static void create_player(const string& tag, Reader& reader, Writer& writer)
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

	writer.Write(tag);
	writer.Write(Hash::OK);
	DatabaseCommit();
}

static void game_operation(const string& tag, Reader& reader, Writer& writer)
{
	string cookie = reader.ReadString();
	Database::Type playeroid = CheckAuthenticationCookie(cookie);
	if (!playeroid)
	{
		Log() << "received invalid auth cookie: " << playeroid;
		throw Hash::AuthenticationFailure;
	}

	double lastupdate = reader.ReadNumber();

	Hash::Type gamecommand = reader.ReadHash();
	SPlayer* player = SPlayer::Get(playeroid);

	Log() << "gamecommand " << gamecommand << " from " << (string)player->PlayerName
			<< " last update time " << lastupdate;

	try
	{
		GameOperation(player, gamecommand);

		/* The operation succeeded. Recalculate the player's visible objects
		 * list.
		 */

		Datum::ObjectSet newVisible;
		Log() << "calculating visible objects";
		player->CalculateVisibleObjects(newVisible);
		Log() << "found " << newVisible.size() << " objects";

		/* This is stupidly heavyweight, but it's the cleanest way to do it. */

		Datum::ObjectSet oldVisible(player->VisibleObjects.SetBegin(),
				player->VisibleObjects.SetEnd());
		if (oldVisible != newVisible)
		{
			Log() << "visible object set changed!";

			player->VisibleObjects.ClearSet();
			for (Datum::ObjectSet::const_iterator i = newVisible.begin(),
					e = newVisible.end(); i != e; i++)
				player->VisibleObjects.AddToSet(*i);
		}

		writer.Write(tag);
		writer.Write(Hash::OK);
		DatabaseCommit();
	}
	catch (Hash::Type error)
	{
		Log() << "gameoperation threw " << tag;
		writer.Write(tag);
		writer.Write(error);
		DatabaseRollback();
	}

	/* Send database updates to the client (even on error). */

	writer.Write(CurrentCanonicalTime());

	for (Datum::ObjectSet::const_iterator i = player->VisibleObjects.SetBegin(),
			e = player->VisibleObjects.SetEnd(); i != e; i++)
	{
		Database::Type owner = DatabaseGet(*i, Hash::Owner);
		DatabaseWriteChangedDatums(writer, *i, (owner == playeroid), lastupdate);
	}
}

class Subtransport : public Transport
{
public:
	Subtransport(const set<string>& endpoints):
		Transport(endpoints)
	{}

	void Request(Reader& reader, Writer& writer)
	{
		UpdateCanonicalTime();

		string tag = reader.ReadString();

		try
		{
			Hash::Type command = reader.ReadHash();
			Log() << "received message tag " << tag << " command: " << command;

			Hash::Type resultcode;
			switch (command)
			{
				case Hash::CreatePlayer:
					create_player(tag, reader, writer);
					break;

				case Hash::Authenticate:
				{
					string s = CreateAuthenticationCookie(reader);
					writer.Write(tag);
					writer.Write(Hash::OK);
					writer.Write(s);
					break;
				}

				case Hash::GameOperation:
				{
					game_operation(tag, reader, writer);
					break;
				}

				default:
					throw Hash::MalformedCommand;
			}
		}
		catch (Hash::Type error)
		{
			Log() << "command threw error: " << error;

			DatabaseRollback();
			writer.Write(tag);
			writer.Write(error);
		}

		Log() << "finished command, waiting...";
//		SaveDatabaseToFile("snapshot.db");
	}
};

void Mainloop(const set<string>& endpoints)
{
	Subtransport t(endpoints);
	t.Mainloop();
}
