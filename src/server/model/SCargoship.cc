#include "globals.h"
#include "SCargoship.h"

SCargoship::SCargoship(Database::Type oid):
	SShip(oid),
	SCargoshipProperties(oid)
{
}

void SCargoship::Initialise(Database::Type owner)
{
	SShip::Initialise(owner);
	CargoM->Dirty();
	CargoA->Dirty();
	CargoO->Dirty();
}
