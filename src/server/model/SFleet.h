#ifndef SFLEET_H
#define SFLEET_H

#include "SObject.h"

class SFleet : public SObject, public SFleetProperties
{
public:
	SFleet(Database::Type oid);

	Hash::Type GetClass()
	{ return Hash::SFleet; }

	Database::Type CreateJumpship();
};

#endif
