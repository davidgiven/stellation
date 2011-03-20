#include "globals.h"
#include "SPlayer.h"
#include "Reader.h"
#include "engine.h"

template <class T>
static T* readRW(Reader& reader, SPlayer* player)
{
	Database::Type oid = reader.ReadOid();
	return player->AccessRW<T>(oid);
}

static string read(Reader& reader)
{
	if (reader.IsEOF())
		throw Hash::MalformedCommand;
	return reader.ReadString();
}

static void endread(Reader& reader)
{
	if (!reader.IsEOF())
		throw Hash::MalformedCommand;
}

void GameOperation(Reader& reader, SPlayer* player, Hash::Type command)
{
	switch (command)
	{
		case Hash::Ping:
			/* Do nothing. */
			break;

		case Hash::Rename:
		{
			SObject* o = readRW<SObject>(reader, player);
			string newname = read(reader);
			endread(reader);

			*o->Name = newname;

			break;
		}

		case Hash::ShutdownServer:
			exit(0);

		default:
			throw Hash::MalformedCommand;
	}
}
