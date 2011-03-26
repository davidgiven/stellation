#include "globals.h"
#include "Database.h"
#include "LazyDatum.h"

static set<LazyDatumBase*> datums;

LazyDatumBase::LazyDatumBase(Database::Type oid, Hash::Type kid):
	_oid(oid),
	_kid(kid),
	_object(NULL)
{
//	printf("lazydatumbase %p\n", this);
}

LazyDatumBase::~LazyDatumBase()
{
//	printf("~lazydatumbase %p\n", this);
	if (_object)
		DetachFromDatabase();
}

void LazyDatumBase::Reset()
{
	_object = NULL;
	DetachFromDatabase();
}

Datum* LazyDatumBase::datum()
{
	if (!_object)
	{
		_object = DatabaseGet(_oid, _kid);
		AttachToDatabase();
	}
	return _object;
}

void LazyDatumBase::AttachToDatabase()
{
	datums.insert(this);
}

void LazyDatumBase::DetachFromDatabase()
{
	datums.erase(this);
}

void LazyDatumBase::FlushCache()
{
	while (!datums.empty())
	{
		LazyDatumBase* ld = *datums.begin();
//		printf("flushing %p\n", ld);
		ld->Reset();
	}
}
