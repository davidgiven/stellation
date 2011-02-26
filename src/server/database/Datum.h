#ifndef DATUM_H
#define DATUM_H

#include "Hash.h"
#include "Database.h"
#include <boost/variant/variant.hpp>

class SObject;

class Datum : public noncopyable
{
public:
	typedef set<Database::Type> ObjectSet;
	typedef map<string, Database::Type> ObjectMap;

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
		OBJECTSET,
		OBJECTMAP
	};

	Database::Type GetOid() const
	{ return _oid; }

	Hash::Type GetKid() const
	{ return _kid; }

	void SetOidKid(Database::Type oid, Hash::Type kid);

	Type GetType() const { return _type; }
	void SetType(Type type);

	double LastChangeTime() const
	{ return _lastChanged; }

	bool ChangedSince(double t) const
	{ return _lastChanged > t; }

	void Dirty();

	Database::Type GetObject() const;
	void SetObject(Database::Type oid);
	void SetObject(SObject* oid);

	double GetNumber() const;
	void SetNumber(double d);

	const string& GetString() const;
	void SetString(const string& s);

	Hash::Type GetToken() const;
	void SetToken(Hash::Type t);

	void AddToSet(Database::Type o);
	void AddToSet(SObject* o);
	void RemoveFromSet(Database::Type o);
	void RemoveFromSet(SObject* o);
	bool InSet(Database::Type o);
	bool InSet(SObject* o);
	void ClearSet();
	int SetLength() const;
	Database::Type RandomSetMember() const;
	ObjectSet::const_iterator SetBegin() const;
	ObjectSet::const_iterator SetEnd() const;

	void AddToMap(const string& key, Database::Type o);
	void AddToMap(const string& key, SObject* o);
	void RemoveFromMap(const string& key);
	Database::Type FetchFromMap(const string& key);
	int MapLength() const;
	ObjectMap::const_iterator MapBegin() const;
	ObjectMap::const_iterator MapEnd() const;

	Datum& operator = (SObject* o)          { SetObject(o); return *this; }
	Datum& operator = (Database::Type o)    { SetObject(o); return *this; }
	Datum& operator = (double d)            { SetNumber(d); return *this; }
	Datum& operator = (const string& s)     { SetString(s); return *this; }
	Datum& operator = (Hash::Type t)        { SetToken(t); return *this; }

	operator Database::Type () const        { return GetObject(); }
	operator double () const                { return GetNumber(); }
	operator const string& () const         { return GetString(); }
	operator Hash::Type () const            { return GetToken(); }

	void Write(Writer& writer) const;

private:
	void CheckType(Type type) const;

private:
	scalar<Type> _type;
	scalar<Database::Type> _oid;
	scalar<Hash::Type> _kid;
	scalar<double> _lastChanged;

	boost::variant<
		Database::Type,
		double,
		string,
		Hash::Type,
		ObjectSet,
		ObjectMap
	> _value;
};

#endif
