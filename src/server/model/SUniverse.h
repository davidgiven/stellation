#ifndef SUNIVERSE_H
#define SUNIVERSE_H

#include "SObject.h"

class SUniverse : public SObject, public SUniverseProperties
{
	CLASSLINK(SUniverse)

public:
	SUniverse(Database::Type oid);
};

#endif
