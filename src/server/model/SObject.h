#ifndef SOBJECT_H
#define SOBJECT_H

#include "LazyDatum.h"
#include "property-accessors-h.h"
#include <boost/cast.hpp>

#define CLASSLINK(C) \
	public: \
		static C* Get(SObject* o) \
		{ return boost::polymorphic_downcast<C*>(o); } \
		\
		static C* Get(LazyDatum<ObjectDatum>& o) \
		{ return boost::polymorphic_downcast<C*>(SObject::Get(*o)); } \
		\
		static C* Get(Database::Type oid) \
		{ return boost::polymorphic_downcast<C*>(SObject::Get(oid)); } \
		\
		static C* Create(Database::Type owner) \
		{ return boost::polymorphic_downcast<C*>(SObject::Create(Hash::C, owner)); } \
		\
		Hash::Type GetClass() const \
		{ return Hash::C; }

class SObject : public noncopyable, public SObjectProperties
{
public:
	typedef Datum::ObjectSet ObjectSet;
	typedef Datum::ObjectMap ObjectMap;

	static Hash::Type GetClass(Database::Type oid);
	static SObject* Get(Database::Type oid);
	static SObject* Create(Hash::Type classtoken, Database::Type owner);
	static void FlushCache();

public:
	SObject(Database::Type oid);
	virtual ~SObject();

	virtual Hash::Type GetClass() const
	{ return Hash::SObject; }

	operator Database::Type () const
	{ return _oid; }

	virtual void Initialise(Database::Type owner);

	void Add(SObject* o);
	void Remove(SObject* o);

	virtual void OnAdditionOf(SObject* o);
	virtual void OnRemovalOf(SObject* o);

	double GetNumberStatic(Hash::Type kid);
	string GetStringStatic(Hash::Type kid);

public:
	Datum* Get(Hash::Type key)
	{ return DatabaseGet(_oid, key); }

private:
	scalar<Database::Type> _oid;
};

#endif
