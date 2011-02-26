#ifndef SJUMPSHIP_H
#define SJUMPSHIP_H

#include "SShip.h"

class SJumpship : public SShip, public SJumpshipProperties
{
	CLASSLINK(SJumpship)

public:
	SJumpship(Database::Type oid);
};

#endif
