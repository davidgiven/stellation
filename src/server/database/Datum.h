#ifndef DATUM_H
#define DATUM_H

class DatabaseObject;

class Datum : public noncopyable
{
public:
	Datum();

	enum Type
	{
		UNSET,
		OBJECT,
		NUMBER,
		STRING,
		OBJECTSET
	};

	enum Scope
	{
		GLOBAL,
		LOCAL,
		PRIVATE
	};

	Type GetType() const
	{ return _type; }

	void SetType(Type type);

	Scope GetScope() const
	{ return _scope; }

	void SetScope(Scope scope)
	{ _scope = scope; }

	DatabaseObject& GetObject() const;
	void SetObject(DatabaseObject& d);

	double GetNumber() const;
	void SetNumber(double d);

	const string& GetString() const;
	void SetString(const string& s);

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

	operator DatabaseObject& () const       { return GetObject(); }
	operator double () const                { return GetNumber(); }
	operator const string& () const         { return GetString(); }

private:
	void CheckType(Type type) const;

private:
	scalar<Type> _type;
	scalar<Scope> _scope;
	int _oid;
	double _number;
	string _string;
	ObjectSet _objectset;
};

#endif
