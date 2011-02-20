#ifndef SSTAR_H
#define SSTAR_H

#include "SObject.h"

class SStar : public SObject
{
public:
	SStar(DatabaseObject& dbo);

	const char* GetType()
	{ return "star"; }

	virtual void Initialise();
};

#endif
