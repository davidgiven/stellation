#ifndef SUNIT_H
#define SUNIT_H

#include "SObject.h"

class SUnit : public SObject, public SUnitProperties
{
	CLASSLINK(SUnit)

public:
	SUnit(Database::Type oid);

	void Initialise(Database::Type owner);
};

#endif
