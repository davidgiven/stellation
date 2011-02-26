#ifndef SUNIT_H
#define SUNIT_H

#include "SObject.h"

class SUnit : public SObject, public SUnitProperties
{
public:
	SUnit(Database::Type oid);

	Hash::Type GetClass()
	{ return Hash::SUnit; }

	void Initialise(Database::Type owner);
};

#endif
