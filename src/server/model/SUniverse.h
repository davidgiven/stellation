#ifndef SUNIVERSE_H
#define SUNIVERSE_H

#include "SObject.h"

class SUniverse : public SObject, public SUniverseProperties
{
public:
	SUniverse(Database::Type oid);

	Hash::Type GetClass()
	{ return Hash::SUniverse; }
};

#endif
