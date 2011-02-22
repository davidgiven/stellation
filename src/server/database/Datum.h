#ifndef DATUM_H
#define DATUM_H

#include "Hash.h"
class DatabaseObject;

class Datum : public noncopyable
{
public:
	Datum();

	enum Type
	{
		UNSET,
		TOKEN,
		OBJECT,
		NUMBER,
		STRING,
		OBJECTSET
	};

	Type GetType() const
	{ return _type; }

	void SetType(Type type);

	DatabaseObject& GetObject() const;
	int GetObjectAsOid() const;
	void SetObject(DatabaseObject& d);

	double GetNumber() const;
	void SetNumber(double d);

	const string& GetString() const;
	void SetString(const string& s);

	Hash::Type GetToken() const;
	void SetToken(Hash::Type t);

	typedef set<int> ObjectSet;

	void AddToSet(DatabaseObject& o);
	void RemoveFromSet(DatabaseObject& o);
	bool InSet(DatabaseObject& o);
	int SetLength() const;
	ObjectSet::const_iterator SetBegin() const;
	ObjectSet::const_iterator SetEnd() const;

	Datum& operator = (DatabaseObject& o)   { SetObject(o); return *this; }
	Datum& operator = (double d)            { SetNumber(d); return *this; }
	Datum& operator = (const string& s)     { SetString(s); return *this; }
	Datum& operator = (Hash::Type t)        { SetToken(t); return *this; }

	operator DatabaseObject& () const       { return GetObject(); }
	operator double () const                { return GetNumber(); }
	operator const string& () const         { return GetString(); }
	operator Hash::Type () const            { return GetToken(); }

private:
	void CheckType(Type type) const;

private:
	scalar<Type> _type;
	scalar<int> _oid;
	scalar<double> _number;
	string _string;
	scalar<Hash::Type> _token;
	ObjectSet _objectset;
};

#endif
