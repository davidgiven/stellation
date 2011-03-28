#ifndef SFLEET_H
#define SFLEET_H

#include "SObject.h"
class SJumpship;
class STug;

class SFleet : public SObject, public SFleetProperties
{
	CLASSLINK(SFleet)

public:
	SFleet(Database::Type oid);

	void OnAdditionOf(SObject* o);
	void OnRemovalOf(SObject* o);

	template <class T> T* CreateShip()
	{
		T* ship = T::Create(*Owner);
		Add(ship);
		return ship;
	}
};

#endif
