#ifndef LAZYDATUM_H
#define LAZYDATUM_H

#include "DatabaseObject.h"

class Datum;

class LazyDatum : public noncopyable
{
public:
	LazyDatum(DatabaseObject& dbo, int hash):
		_dbo(dbo),
		_hash(hash),
		_datum(NULL)
	{}

private:
	DatabaseObject& _dbo;
	scalar<int> _hash;
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

	int GetType()                           { return datum().GetType(); }
	void SetType(int type)                  { return datum().SetType(type); }

	Datum& operator = (double d)            { return datum() = d; }
	Datum& operator = (const string& s)     { return datum() = s; }

	operator double ()                      { return datum(); }
	operator const string& ()               { return datum(); }

	void AddToSet(DatabaseObject& o)        { datum().AddToSet(o); }
	void RemoveFromSet(DatabaseObject& o)   { datum().RemoveFromSet(o); }
	bool InSet(DatabaseObject& o)           { return datum().InSet(o); }
};

#endif
