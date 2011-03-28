#ifndef SCARGOSHIP_H
#define SCARGOSHIP_H

#include "SShip.h"

class SCargoship : public SShip, public SCargoshipProperties
{
	CLASSLINK(SCargoship)

public:
	SCargoship(Database::Type oid);

	void Initialise(Database::Type owner);
};

#endif
