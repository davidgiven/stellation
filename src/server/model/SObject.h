#ifndef SOBJECT_H
#define SOBJECT_H

#include "LazyDatum.h"
#include "DatabaseObject.h"

class SObject : public noncopyable
{
public:
	SObject(DatabaseObject& dbo);

	virtual const char* GetType()
	{ return "object"; }

	operator DatabaseObject& () const
	{ return _dbo; }

	virtual void Initialise();

	void Add(SObject& o);
	void Remove(SObject& o);

	virtual void OnAdditionOf(SObject& o);
	virtual void OnRemovalOf(SObject& o);

public:
	Datum& Get(int key)
	{ return _dbo.Get(key); }

#include "accessors.h"

private:
	DatabaseObject& _dbo;
};

#endif
