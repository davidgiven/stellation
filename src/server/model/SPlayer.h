#ifndef SPLAYER_H
#define SPLAYER_H

#include "SObject.h"
class SStar;
class SFleet;

class SPlayer : public SObject, public SPlayerProperties
{
	CLASSLINK(SPlayer, SObject, SPlayerProperties)

public:
	typedef map<Database::Type, Database::Visibility> VisibilityMap;

public:
	SPlayer(Database::Type oid);

	template <class S>
	S* AccessRW(Database::Type oid)
	{
		AccessRW(oid);
		return S::Get(oid);
	}

	void AccessRW(Database::Type oid);

	SFleet* CreateFleet(SStar* location, const string& name);

	void CalculateVisibleObjects(VisibilityMap& visible);
};

#endif
