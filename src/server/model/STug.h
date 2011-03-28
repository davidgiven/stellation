#ifndef STUG_H
#define STUG_H

#include "SShip.h"

class STug : public SShip, public STugProperties
{
	CLASSLINK(STug, SShip, STugProperties)

public:
	STug(Database::Type oid);
};

#endif
