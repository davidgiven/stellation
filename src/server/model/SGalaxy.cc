#include "globals.h"
#include "SGalaxy.h"
#include "SStar.h"

SGalaxy::SGalaxy(Database::Type oid):
	SObject(oid),
	SGalaxyProperties(oid)
{
}
