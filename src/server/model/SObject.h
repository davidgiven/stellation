#ifndef SOBJECT_H
#define SOBJECT_H

#include "LazyDatum.h"

class DatabaseObject;

class SObject
{
public:
	SObject(DatabaseObject& dbo);

public:
	Datum& Get(int key)
	{ return _dbo.Get(key); }

#include "accessors.h"

private:
	DatabaseObject& _dbo;
};

#endif
