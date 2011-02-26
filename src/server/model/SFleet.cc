#include "globals.h"
#include "SJumpship.h"
#include "SFleet.h"

SFleet::SFleet(Database::Type oid):
	SObject(oid),
	SFleetProperties(oid)
{
}

Database::Type SFleet::CreateJumpship()
{
	SJumpship jumpship(DatabaseAllocateOid());
	jumpship.Initialise(Owner);

	Add(jumpship);

	return jumpship;
}
