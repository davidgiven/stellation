#include "globals.h"
#include "SUnit.h"

SUnit::SUnit(Database::Type oid):
	SObject(oid),
	SUnitProperties(oid)
{
}
