#ifndef SGALAXY_H
#define SGALAXY_H

#include "SObject.h"

class SGalaxy : public SObject, public SGalaxyProperties
{
	CLASSLINK(SGalaxy)

public:
	SGalaxy(Database::Type oid);
};

#endif
