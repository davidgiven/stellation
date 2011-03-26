#include "globals.h"
#include "mainloop.h"
#include "Log.h"
#include "Hash.h"
#include "Reader.h"
#include "Writer.h"
#include "Transport.h"
#include "Property.h"
#include "SPlayer.h"
#include "worldcreation.h"
#include "auth.h"
#include "engine.h"
#include "utils.h"
#include "statics.h"
#include <deque>

static void create_player(const string& tag, Reader& reader, Writer& writer)
{
	string name;
	string empirename;
	string email;
	string password;

	while (!reader.IsEOF())
	{
		Hash::Type key = reader.ReadHash();
		string value = reader.ReadString();

		switch (key)
		{
			case Hash::Name:           name = value; break;
			case Hash::EmpireName:     empirename = value; break;
			case Hash::Email:          email = value; break;
			case Hash::Password:       password = value; break;

			default:
				throw Hash::MalformedCommand;
		}
	}

	CreatePlayer(name, empirename, email, password);

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

	try
	{
		SPlayer* player = SPlayer::Get(playeroid);

		Log() << "gamecommand " << gamecommand << " from " << *player->Name
				<< " last update time " << lastupdate;

		GameOperation(reader, player, gamecommand);

		/* The operation succeeded. Recalculate the player's visible objects
		 * list.
		 */

		/* This is stupidly heavyweight, but it's the cleanest way to do it. */


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

	/* Calculate the player's set of visible objects. */

	SPlayer::VisibilityMap newVisible;

	Log() << "calculating visible objects";
	SPlayer* player = SPlayer::Get(playeroid);
	player->CalculateVisibleObjects(newVisible);
	Log() << "found " << newVisible.size() << " objects";

	Datum::ObjectSet newVisibleSet;
	for (SPlayer::VisibilityMap::const_iterator i = newVisible.begin(),
			e = newVisible.end(); i != e; i++)
	{
		newVisibleSet.insert(i->first);
	}

	Datum::ObjectSet oldVisibleSet(player->VisibleObjects->SetBegin(),
			player->VisibleObjects->SetEnd());
	if (oldVisibleSet != newVisibleSet)
	{
		Log() << "visible object set changed!";

		player->VisibleObjects->ClearSet();
		for (Datum::ObjectSet::const_iterator i = newVisibleSet.begin(),
				e = newVisibleSet.end(); i != e; i++)
			player->VisibleObjects->AddToSet(*i);
		DatabaseCommit();
	}
	/* Send database updates to the client (even on error). */

	writer.Write(CurrentCanonicalTime());

	for (SPlayer::VisibilityMap::const_iterator i = newVisible.begin(),
			e = newVisible.end(); i != e; i++)
	{
		ObjectDatum* owner = (ObjectDatum*) DatabaseGet(i->first, Hash::Owner);

		Database::Visibility v = i->second;
		if (*owner == playeroid)
			v = Database::OwnerVisibility;
		DatabaseWriteChangedDatums(writer, i->first, v, lastupdate);
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

					Database::Type playeroid = CheckAuthenticationCookie(s);
					writer.Write(playeroid);
					break;
				}

				case Hash::GetStatic:
				{
					writer.Write(tag);
					writer.Write(Hash::OK);
					WritePropertyInfo(writer);
					WriteAllStatics(writer);
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
