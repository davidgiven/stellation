#ifndef SPLAYER_H
#define SPLAYER_H

#include "SObject.h"
class SStar;
class SFleet;

class SPlayer : public SObject, public SPlayerProperties
{
	CLASSLINK(SPlayer)

public:
	SPlayer(Database::Type oid);

	SFleet* CreateFleet(SStar* location, const string& name);

	void CalculateVisibleObjects(ObjectSet& visible);
};

#endif
