#include "globals.h"
#include "SJumpship.h"

SJumpship::SJumpship(Database::Type oid):
	SShip(oid),
	SJumpshipProperties(oid)
{
}
