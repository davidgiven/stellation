#ifndef LAZYDATUM_H
#define LAZYDATUM_H

#include "DatabaseObject.h"

class Datum;

class LazyDatum : public noncopyable
{
public:
	LazyDatum(DatabaseObject& dbo, Hash::Type hash):
		_dbo(dbo),
		_hash(hash),
		_datum((Datum*) NULL)
	{}

private:
	DatabaseObject& _dbo;
	scalar<Hash::Type> _hash;
	scalar<Datum*> _datum;

private:
	Datum& datum()
	{
		if (!_datum)
			_datum = &_dbo.Get(_hash);
		return *_datum;
	}

public:
	operator Datum& ()                      { return datum(); }

	Datum::Type GetType()                   { return datum().GetType(); }
	void SetType(Datum::Type type)          { return datum().SetType(type); }

	Datum& operator = (DatabaseObject& o)   { return datum() = o; }
	Datum& operator = (double d)            { return datum() = d; }
	Datum& operator = (const string& s)     { return datum() = s; }
	Datum& operator = (Hash::Type t)        { return datum() = t; }

	operator DatabaseObject& ()             { return datum(); }
	operator double ()                      { return datum(); }
	operator const string& ()               { return datum(); }
	operator Hash::Type ()                  { return datum(); }

	void AddToSet(DatabaseObject& o)        { datum().AddToSet(o); }
	void RemoveFromSet(DatabaseObject& o)   { datum().RemoveFromSet(o); }
	bool InSet(DatabaseObject& o)           { return datum().InSet(o); }
};

#endif
