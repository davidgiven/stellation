#ifndef SCARGOSHIP_H
#define SCARGOSHIP_H

#include "SShip.h"

class SCargoship : public SShip, public SCargoshipProperties
{
	CLASSLINK(SCargoship, SShip, SCargoshipProperties)

public:
	SCargoship(Database::Type oid);
};

#endif
