#ifndef OBJECT_H
#define OBJECT_H

#include "Datum.h"

class DatabaseObject
{
public:
	DatabaseObject(int oid);
	~DatabaseObject();

	Datum& Get(int key);

	static int HashPropertyName(const string& name);

private:
	scalar<int> _oid;

	typedef map<int, Datum> DatabaseMap;
	DatabaseMap _map;
};

#endif
