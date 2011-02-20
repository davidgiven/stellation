#include "globals.h"
#include "DatabaseObject.h"
#include "Datum.h"

#include "hash.h"

static const int type_hash = propertyHash("type", 4);

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

const char* DatabaseObject::PropertyNameFromHash(int hash)
{
	assert((hash >= MIN_HASH_VALUE) && (hash <= MAX_HASH_VALUE));
	return propertyNameTable[hash];
}

const string& DatabaseObject::GetType()
{
	return Get(type_hash);
}

Datum& DatabaseObject::Get(int key)
{
	Map::const_iterator i = _map.find(key);
	if (i == _map.end())
	{
		shared_ptr<Datum> o(new Datum());
		_map[key] = o;

		return *o;
	}
	return *(i->second);
}
