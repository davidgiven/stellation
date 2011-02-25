#ifndef DATUM_H
#define DATUM_H

#include "Hash.h"
#include "Database.h"
#include <boost/variant.hpp>

class Datum : public noncopyable
{
public:
	Datum();
	Datum(Database::Type oid, Hash::Type name);
	Datum(const Datum& other);

	Datum& operator = (const Datum& other);

	enum Type
	{
		UNSET,
		TOKEN,
		OBJECT,
		NUMBER,
		STRING,
		OBJECTSET
	};

	Database::Type GetOid() const
	{ return _oid; }

	Hash::Type GetKid() const
	{ return _kid; }

	void SetOidKid(Database::Type oid, Hash::Type kid);

	Type GetType() const
	{ return _type; }

	void SetType(Type type);

	void Dirty();

	Database::Type GetObject() const;
	void SetObject(Database::Type oid);

	double GetNumber() const;
	void SetNumber(double d);

	const string& GetString() const;
	void SetString(const string& s);

	Hash::Type GetToken() const;
	void SetToken(Hash::Type t);

	typedef set<Database::Type> ObjectSet;

	void AddToSet(Database::Type o);
	void RemoveFromSet(Database::Type o);
	bool InSet(Database::Type o);
	int SetLength() const;
	ObjectSet::const_iterator SetBegin() const;
	ObjectSet::const_iterator SetEnd() const;

	Datum& operator = (Database::Type o)    { SetObject(o); return *this; }
	Datum& operator = (double d)            { SetNumber(d); return *this; }
	Datum& operator = (const string& s)     { SetString(s); return *this; }
	Datum& operator = (Hash::Type t)        { SetToken(t); return *this; }

	operator Database::Type () const        { return GetObject(); }
	operator double () const                { return GetNumber(); }
	operator const string& () const         { return GetString(); }
	operator Hash::Type () const            { return GetToken(); }

private:
	void CheckType(Type type) const;

private:
	scalar<Type> _type;
	scalar<Database::Type> _oid;
	scalar<Hash::Type> _kid;

	boost::variant<
		Database::Type,
		double,
		string,
		Hash::Type,
		ObjectSet> _value;
};

#endif
