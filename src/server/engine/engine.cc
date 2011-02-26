#include "globals.h"
#include "SPlayer.h"
#include "engine.h"

void GameOperation(SPlayer* player, Hash::Type command)
{
	switch (command)
	{
		case Hash::Ping:
			/* Do nothing. */
			break;

		case Hash::ShutdownServer:
			exit(0);

		default:
			throw Hash::MalformedCommand;
	}
}
