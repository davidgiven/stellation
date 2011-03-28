#include "globals.h"
#include "SUnit.h"
#include "statics.h"

SUnit::SUnit(Database::Type oid):
	SObject(oid),
	SUnitProperties(oid)
{
}

void SUnit::Initialise()
{
	SObject::Initialise();

	*Damage = 0.0;
	*MaxDamage = GetNumberStatic(Hash::MaxDamage);
	*Mass = GetNumberStatic(Hash::Mass);
}

