#ifndef SOBJECT_H
#define SOBJECT_H

#include "LazyDatum.h"
#include "property-accessors-h.h"

class SObject : public noncopyable, public SObjectProperties
{
public:
	SObject(Database::Type oid);

	virtual Hash::Type GetClass()
	{ return Hash::SObject; }

	operator Database::Type () const
	{ return _oid; }

	virtual void Initialise(Database::Type owner);

	void Add(SObject& o);
	void Remove(SObject& o);

	virtual void OnAdditionOf(SObject& o);
	virtual void OnRemovalOf(SObject& o);

	double GetNumberStatic(Hash::Type kid);
	string GetStringStatic(Hash::Type kid);

public:
	Datum& Get(Hash::Type key)
	{ return DatabaseGet(_oid, key); }

private:
	scalar<Database::Type> _oid;
};

#endif
