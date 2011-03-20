#ifndef LAZYDATUM_H
#define LAZYDATUM_H

#include "Database.h"
#include "Hash.h"
#include "Datum.h"

class LazyDatumBase : public noncopyable
{
public:
	typedef Datum::ObjectSet ObjectSet;
	typedef Datum::ObjectMap ObjectMap;

	static void FlushCache();

public:
	LazyDatumBase(Database::Type oid, Hash::Type kid);
	virtual ~LazyDatumBase();

protected:
	void AttachToDatabase();
	void DetachFromDatabase();
	void Reset();

protected:
	Datum* datum();

private:
	scalar<Database::Type> _oid;
	scalar<Hash::Type> _kid;
	shared_ptr<Datum> _object;
};

template <class T>
class LazyDatum : public LazyDatumBase
{
public:
	LazyDatum(Database::Type oid, Hash::Type kid):
		LazyDatumBase(oid, kid)
	{}

private:
	T* datum()
	{
		return (T*) LazyDatumBase::datum();
	}

public:
	T& operator* ()
	{
		return *datum();
	}

	T* operator-> ()
	{
		return datum();
	}
};

#endif
