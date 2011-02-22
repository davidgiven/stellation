#ifndef SSTAR_H
#define SSTAR_H

#include "SObject.h"

class SStar : public SObject
{
public:
	SStar(DatabaseObject& dbo);

	Hash::Type GetClass()
	{ return Hash::SStar; }
};

#endif
