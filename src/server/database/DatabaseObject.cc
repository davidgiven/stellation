#include "globals.h"
#include "DatabaseObject.h"

#include "hash.h"

DatabaseObject::DatabaseObject(int oid):
	_oid(oid)
{
}

DatabaseObject::~DatabaseObject()
{
}

int DatabaseObject::HashPropertyName(const string& s)
{
	return propertyHash(s.data(), s.size());
}

Datum& DatabaseObject::Get(int key)
{
	return _map[key];
}
