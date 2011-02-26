#ifndef SFLEET_H
#define SFLEET_H

#include "SObject.h"
class SJumpship;

class SFleet : public SObject, public SFleetProperties
{
	CLASSLINK(SFleet)

public:
	SFleet(Database::Type oid);

	void OnAdditionOf(SObject* o);
	void OnRemovalOf(SObject* o);

	SJumpship* CreateJumpship();
};

#endif
