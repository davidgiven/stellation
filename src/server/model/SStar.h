#ifndef SSTAR_H
#define SSTAR_H

#include "SObject.h"

class SStar : public SObject, public SStarProperties
{
public:
	SStar(Database::Type oid);

	Hash::Type GetClass()
	{ return Hash::SStar; }
};

#endif
