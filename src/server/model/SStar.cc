#include "globals.h"
#include "Log.h"
#include "SPlayer.h"
#include "SFleet.h"
#include "SUnit.h"
#include "SStar.h"

SStar::SStar(Database::Type oid):
	SObject(oid),
	SStarProperties(oid)
{
}
