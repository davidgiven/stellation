#ifndef OBJECT_H
#define OBJECT_H

#include "Datum.h"

class DatabaseObject : public noncopyable
{
public:
	DatabaseObject(int oid);
	~DatabaseObject();

	Datum& Get(int key);

	const string& GetType();

	operator int () const
	{ return _oid; }

	static int HashPropertyName(const string& name);
	static const char* PropertyNameFromHash(int hash);

	typedef map<int, shared_ptr<Datum> > Map;

	Map::const_iterator Begin() const
	{ return _map.begin(); }

	Map::const_iterator End() const
	{ return _map.end(); }

private:
	const scalar<int> _oid;
	Map _map;
};

#endif
