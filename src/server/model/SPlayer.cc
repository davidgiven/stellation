#include "globals.h"
#include "SPlayer.h"

SPlayer::SPlayer(Database::Type oid):
	SObject(oid),
	SPlayerProperties(oid)
{
}
