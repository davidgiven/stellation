#include "globals.h"
#include "SStar.h"
#include "SFleet.h"
#include "SPlayer.h"

SPlayer::SPlayer(Database::Type oid):
	SObject(oid),
	SPlayerProperties(oid)
{
}

Database::Type SPlayer::CreateFleet(SStar& location, const string& name)
{
	if (Fleets.FetchFromMap(name))
		throw Hash::FleetAlreadyHasThisName;

	SFleet fleet(DatabaseAllocateOid());
	fleet.Initialise(*this);
	fleet.FleetName = name;
	fleet.Location = location;
	location.Add(fleet);

	Fleets.AddToMap(name, fleet);

	return fleet;
}
