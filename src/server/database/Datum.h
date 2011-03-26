#ifndef DATUM_H
#define DATUM_H

#include "Hash.h"
#include "Database.h"

class SObject;
class Reader;
class Writer;

class Datum : public noncopyable
{
public:
	typedef set<Database::Type> ObjectSet;
	typedef map<string, Database::Type> ObjectMap;

public:
	static Datum* Create(Database::Type oid, Hash::Type kid);

public:
	Datum(Database::Type oid, Hash::Type kid);
	virtual ~Datum();

	enum Type
	{
		UnsetDatumType = Hash::Null,
		TokenDatumType = Hash::Token,
		ObjectDatumType = Hash::Object,
		NumberDatumType = Hash::Number,
		StringDatumType = Hash::String,
		ObjectSetDatumType = Hash::ObjectSet,
		ObjectMapDatumType = Hash::ObjectMap
	};

	Database::Type GetOid() const
	{ return _oid; }

	Hash::Type GetKid() const
	{ return _kid; }

	void SetOidKid(Database::Type oid, Hash::Type kid);

	unsigned LastChangeTime() const
	{ return _lastChanged; }

	void SetLastChangeTime(unsigned t)
	{ _lastChanged = t; }

	bool ChangedSince(unsigned t) const
	{ return _lastChanged > t; }

	void Dirty();

	virtual void Read(Reader& reader) = 0;
	virtual void Write(Writer& writer) const = 0;

private:
	scalar<Database::Type> _oid;
	scalar<Hash::Type> _kid;
	scalar<unsigned> _lastChanged;
};

class ObjectDatum : public Datum
{
public:
	ObjectDatum(Database::Type oid, Hash::Type name):
		Datum(oid, name),
		_value(Database::Null)
	{}

public:
	Database::Type GetObject() const             { return _value; };
	void SetObject(Database::Type oid);
	void SetObject(SObject* oid);

	ObjectDatum& operator = (SObject* o)         { SetObject(o); return *this; }
	ObjectDatum& operator = (Database::Type o)   { SetObject(o); return *this; }
	operator Database::Type () const             { return GetObject(); }

	void Read(Reader& reader);
	void Write(Writer& writer) const;

private:
	scalar<Database::Type> _value;
};

class TokenDatum : public Datum
{
public:
	TokenDatum(Database::Type oid, Hash::Type name):
		Datum(oid, name),
		_value(Hash::Null)
	{}

public:
	Hash::Type GetToken() const                  { return _value; }
	void SetToken(Hash::Type t);

	TokenDatum& operator = (Hash::Type t)        { SetToken(t); return *this; }
	operator Hash::Type () const                 { return GetToken(); }

	void Read(Reader& reader);
	void Write(Writer& writer) const;

private:
	scalar<Hash::Type> _value;
};

class StringDatum : public Datum
{
public:
	StringDatum(Database::Type oid, Hash::Type name):
		Datum(oid, name)
	{}

public:
	const string& GetString() const              { return _value; }
	void SetString(const string& s);

	StringDatum& operator = (const string& s)    { SetString(s); return *this; }
	operator const string& () const              { return GetString(); }

	bool operator == (const string& other)       { return GetString() == other; }
	bool operator != (const string& other)       { return GetString() != other; }

	void Read(Reader& reader);
	void Write(Writer& writer) const;

private:
	string _value;
};

class NumberDatum : public Datum
{
public:
	NumberDatum(Database::Type oid, Hash::Type name):
		Datum(oid, name),
		_value(0.0)
	{}

public:
	double GetNumber() const                     { return _value; }
	void SetNumber(double d);

	NumberDatum& operator = (double d)           { SetNumber(d); return *this; }
	operator double () const                     { return GetNumber(); }

	void Read(Reader& reader);
	void Write(Writer& writer) const;

private:
	scalar<double> _value;
};

class ObjectSetDatum : public Datum
{
public:
	ObjectSetDatum(Database::Type oid, Hash::Type name):
		Datum(oid, name)
	{}

public:
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

	void Read(Reader& reader);
	void Write(Writer& writer) const;

private:
	ObjectSet _value;
};

class ObjectMapDatum : public Datum
{
public:
	ObjectMapDatum(Database::Type oid, Hash::Type name):
		Datum(oid, name)
	{}

public:
	void AddToMap(const string& key, Database::Type o);
	void AddToMap(const string& key, SObject* o);
	void RemoveFromMap(const string& key);
	Database::Type FetchFromMap(const string& key);
	int MapLength() const;
	ObjectMap::const_iterator MapBegin() const;
	ObjectMap::const_iterator MapEnd() const;

	void Read(Reader& reader);
	void Write(Writer& writer) const;

private:
	ObjectMap _value;
};

#endif
