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

private:
	const scalar<int> _oid;

	typedef map<int, shared_ptr<Datum> > DatabaseMap;
	DatabaseMap _map;
};

#endif
