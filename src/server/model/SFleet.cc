#include "globals.h"
#include "SJumpship.h"
#include "STug.h"
#include "SFleet.h"

SFleet::SFleet(Database::Type oid):
	SObject(oid),
	SFleetProperties(oid)
{
}

void SFleet::OnAdditionOf(SObject* o)
{
	if (o->GetClass() == Hash::SJumpship)
		*JumpshipCount = *JumpshipCount + 1;
}

void SFleet::OnRemovalOf(SObject* o)
{
	if (o->GetClass() == Hash::SJumpship)
		*JumpshipCount = *JumpshipCount - 1;
}
