#ifndef SSTAR_H
#define SSTAR_H

#include "SObject.h"
class SPlayer;

class SStar : public SObject, public SStarProperties
{
	CLASSLINK(SStar, SObject, SStarProperties)

public:
	SStar(Database::Type oid);
};

#endif
