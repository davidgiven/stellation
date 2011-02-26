#ifndef LAZYDATUM_H
#define LAZYDATUM_H

#include "Database.h"
#include "Hash.h"
#include "Datum.h"

class LazyDatum : public noncopyable
{
public:
	typedef Datum::ObjectSet ObjectSet;
	typedef Datum::ObjectMap ObjectMap;

public:
	LazyDatum(Database::Type oid, Hash::Type kid):
		_oid(oid),
		_kid(kid),
		_datum((Datum*) NULL)
	{}

private:
	scalar<Database::Type> _oid;
	scalar<Hash::Type> _kid;
	scalar<Datum*> _datum;

private:
	Datum& datum()
	{
		if (!_datum)
			_datum = &DatabaseGet(_oid, _kid);
		return *_datum;
	}

public:
	operator Datum& ()                      { return datum(); }

	Datum::Type GetType()                   { return datum().GetType(); }
	void SetType(Datum::Type type)          { return datum().SetType(type); }

	Datum& operator = (Database::Type o)    { return datum() = o; }
	Datum& operator = (double d)            { return datum() = d; }
	Datum& operator = (const string& s)     { return datum() = s; }
	Datum& operator = (Hash::Type t)        { return datum() = t; }

	operator Database::Type ()              { return datum(); }
	operator double ()                      { return datum(); }
	operator const string& ()               { return datum(); }
	operator Hash::Type ()                  { return datum(); }

	void AddToSet(Database::Type o)         { datum().AddToSet(o); }
	void RemoveFromSet(Database::Type o)    { datum().RemoveFromSet(o); }
	bool InSet(Database::Type o)            { return datum().InSet(o); }

	void AddToMap(const string& key, Database::Type o) { datum().AddToMap(key, o); }
	void RemoveFromMap(const string& key)   { datum().RemoveFromMap(key); }
	Database::Type FetchFromMap(const string& key) { return datum().FetchFromMap(key); }
	int MapLength()                         { return datum().MapLength(); }
	ObjectMap::const_iterator MapBegin()    { return datum().MapBegin(); }
	ObjectMap::const_iterator MapEnd()      { return datum().MapEnd(); }

};

#endif
