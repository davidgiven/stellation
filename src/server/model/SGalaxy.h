#ifndef SGALAXY_H
#define SGALAXY_H

#include "SObject.h"

class SGalaxy : public SObject
{
public:
	SGalaxy(DatabaseObject& dbo);

	Hash::Type GetClass()
	{ return Hash::SGalaxy; }

	virtual void OnAdditionOf(SObject& o);
	virtual void OnRemovalOf(SObject& o);
};

#endif
