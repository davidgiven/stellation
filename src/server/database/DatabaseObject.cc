#include "globals.h"
#include "DatabaseObject.h"
#include "Datum.h"

DatabaseObject::DatabaseObject(int oid):
	_oid(oid)
{
}

DatabaseObject::~DatabaseObject()
{
}

Datum& DatabaseObject::Get(Hash::Type key)
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
