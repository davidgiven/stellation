#include "globals.h"
#include "SCargoship.h"

SCargoship::SCargoship(Database::Type oid):
	SShip(oid),
	SCargoshipProperties(oid)
{
}
