#ifndef SUNIT_H
#define SUNIT_H

#include "SObject.h"

class SUnit : public SObject, public SUnitProperties
{
	CLASSLINK(SUnit, SObject, SUnitProperties)

public:
	SUnit(Database::Type oid);

	void Initialise();
};

#endif
