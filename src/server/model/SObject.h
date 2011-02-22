#ifndef SOBJECT_H
#define SOBJECT_H

#include "LazyDatum.h"
#include "DatabaseObject.h"
#include "Property.h"

class SObject : public noncopyable
{
public:
	SObject(DatabaseObject& dbo);

	virtual Hash::Type GetClass()
	{ return Hash::SObject; }

	operator DatabaseObject& () const
	{ return _dbo; }

	virtual void Initialise();

	void Add(SObject& o);
	void Remove(SObject& o);

	virtual void OnAdditionOf(SObject& o);
	virtual void OnRemovalOf(SObject& o);

public:
	Datum& Get(Hash::Type key)
	{ return _dbo.Get(key); }

#include "property-accessors-h.h"

private:
	DatabaseObject& _dbo;
};

#endif