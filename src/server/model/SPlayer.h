#ifndef SPLAYER_H
#define SPLAYER_H

#include "SObject.h"
class SStar;

class SPlayer : public SObject, public SPlayerProperties
{
public:
	SPlayer(Database::Type oid);

	Hash::Type GetClass()
	{ return Hash::SPlayer; }

	Database::Type CreateFleet(SStar& location, const string& name);
};

#endif
