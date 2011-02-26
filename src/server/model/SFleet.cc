#include "globals.h"
#include "SJumpship.h"
#include "SFleet.h"

SFleet::SFleet(Database::Type oid):
	SObject(oid),
	SFleetProperties(oid)
{
}

void SFleet::OnAdditionOf(SObject& o)
{
	if (o.GetClass() == Hash::SJumpship)
		JumpshipCount = (double)JumpshipCount + 1;
}

void SFleet::OnRemovalOf(SObject& o)
{
	if (o.GetClass() == Hash::SJumpship)
		JumpshipCount = (double)JumpshipCount - 1;
}

SJumpship* SFleet::CreateJumpship()
{
	SJumpship* jumpship = SJumpship::Create(Owner);

	Add(jumpship);

	return jumpship;
}
