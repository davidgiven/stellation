#include "globals.h"
#include "Log.h"
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
	for (Datum::ObjectSet::const_iterator i = Fleets.SetBegin(),
			e = Fleets.SetEnd(); i != e; i++)
	{
		SFleet* fleet = SFleet::Get(*i);
		if (fleet->Name == name)
			throw Hash::FleetAlreadyHasThisName;
	}

	SFleet* fleet = SFleet::Create(*this);

	fleet->Name = name;
	location->Add(fleet);

	Fleets.AddToSet(fleet);

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
	for (Datum::ObjectSet::const_iterator i = Fleets.SetBegin(),
			e = Fleets.SetEnd(); i != e; i++)
	{
		SFleet* fleet = SFleet::Get(*i);
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

void SPlayer::AccessRW(Database::Type oid)
{
	if (!DatabaseExists(oid, Hash::Location))
		throw Hash::ObjectDoesNotExist;

	/* Is this object the player? */

	if (oid == *this)
		return;

	/* Is this object in a fleet? */

	while (oid)
	{
		SObject* o = SObject::Get(oid);
		switch (o->Class)
		{
			case Hash::SFleet:
				/* If this fleet belongs to the player *and* it has a
				 * jumpship, the object is accessible. */

				if ((Database::Type)o->Owner == (Database::Type)*this)
				{
					SFleet* fleet = SFleet::Get(o);
					if ((double)fleet->JumpshipCount > 0)
						return;
				}
				break;

			case Hash::SStar:
			{
				/* If this star contains at least one fleet that belongs to
				 * the player *and* which has a jumpship, the object is
				 * accessible.
				 */

				for (ObjectSet::const_iterator i = o->Contents.SetBegin(),
						e = o->Contents.SetEnd(); i != e; i++)
				{
					if (SObject::GetClass(*i) == Hash::SFleet)
					{
						SFleet* fleet = SFleet::Get(*i);
						if ((fleet->Owner == *this) && ((double)fleet->JumpshipCount > 0))
							return;
					}
				}

				break;
			}
		}

		oid = o->Location;
	}

	/* Nope. Note accessible. */

	throw Hash::PrivilegeViolation;
}
