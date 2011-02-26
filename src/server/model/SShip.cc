#include "globals.h"
#include "SShip.h"
#include "statics.h"

SShip::SShip(Database::Type oid):
	SUnit(oid),
	SShipProperties(oid)
{
}
