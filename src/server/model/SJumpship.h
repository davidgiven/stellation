#ifndef SJUMPSHIP_H
#define SJUMPSHIP_H

#include "SUnit.h"

class SJumpship : public SUnit, public SJumpshipProperties
{
public:
	SJumpship(Database::Type oid);

	Hash::Type GetClass()
	{ return Hash::SJumpship; }
};

#endif
