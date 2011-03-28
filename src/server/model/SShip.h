#ifndef SSHIP_H
#define SSHIP_H

#include "SUnit.h"

class SShip : public SUnit, public SShipProperties
{
	CLASSLINK(SShip, SUnit, SShipProperties)

public:
	SShip(Database::Type oid);
};

#endif
