#ifndef SGALAXY_H
#define SGALAXY_H

#include "SObject.h"

class SGalaxy : public SObject
{
public:
	SGalaxy(DatabaseObject& dbo);

	const char* GetType()
	{ return "galaxy"; }

	virtual void Initialise();
	virtual void OnAdditionOf(SObject& o);
	virtual void OnRemovalOf(SObject& o);
};

#endif
