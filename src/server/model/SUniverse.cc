#include "globals.h"
#include "SUniverse.h"
#include "SStar.h"

SUniverse::SUniverse(Database::Type oid):
	SObject(oid),
	SUniverseProperties(oid)
{
}
