#include "globals.h"
#include "SUniverse.h"
#include "SGalaxy.h"
#include "SStar.h"
#include "SFleet.h"
#include "SPlayer.h"

SPlayer::SPlayer(Database::Type oid):
	SObject(oid),
	SPlayerProperties(oid)
{
}

SFleet* SPlayer::CreateFleet(SStar* location, const string& name)
{
	if (Fleets.FetchFromMap(name))
		throw Hash::FleetAlreadyHasThisName;

	SFleet* fleet = SFleet::Create(*this);

	fleet->FleetName = name;
	location->Add(fleet);

	Fleets.AddToMap(name, fleet);

	return fleet;
}

static void add_with_contents(SObject::ObjectSet& visible, SObject* o)
{
	visible.insert(*o);

	for (SObject::ObjectSet::const_iterator i = o->Contents.SetBegin(),
			e = o->Contents.SetEnd(); i != e; i++)
	{
		SObject* child = SObject::Get(*i);
		add_with_contents(visible, child);
	}
}

void SPlayer::CalculateVisibleObjects(ObjectSet& visible)
{
	/* The player and the universe itself are visible. */

	SUniverse* universe = SUniverse::Get(Database::Universe);

	visible.insert(*this);
	visible.insert(*universe);

	/* The galaxy and all (glowing) stars are visible. */

	SGalaxy* galaxy = SGalaxy::Get(universe->Galaxy);
	visible.insert(*galaxy);

	for (Datum::ObjectSet::const_iterator i = galaxy->VisibleStars.SetBegin(),
			e = galaxy->VisibleStars.SetEnd(); i != e; i++)
	{
		visible.insert(*i);
	}

	/* We can see every fleet which has a jumpship. */

	ObjectSet locationsWithAJumpship;
	for (Datum::ObjectMap::const_iterator i = Fleets.MapBegin(),
			e = Fleets.MapEnd(); i != e; i++)
	{
		SFleet* fleet = SFleet::Get(i->second);
		if ((double)fleet->JumpshipCount > 0.0)
			locationsWithAJumpship.insert(fleet->Location);
	}

	for (ObjectSet::const_iterator i = locationsWithAJumpship.begin(),
			e = locationsWithAJumpship.end(); i != e; i++)
	{
		SStar* star = SStar::Get(*i);
		add_with_contents(visible, star);
	}
}
