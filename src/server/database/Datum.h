#ifndef DATUM_H
#define DATUM_H

class DatabaseObject;

class Datum : public noncopyable
{
public:
	Datum();

	enum
	{
		UNSET,
		OBJECT,
		NUMBER,
		STRING,
		OBJECTSET
	};

	int GetType() const
	{ return _type; }

	void SetType(int type);

	DatabaseObject& GetObject() const;
	void SetObject(DatabaseObject& d);

	double GetNumber() const;
	void SetNumber(double d);

	const string& GetString() const;
	void SetString(const string& s);

	void AddToSet(DatabaseObject& o);
	void RemoveFromSet(DatabaseObject& o);
	bool InSet(DatabaseObject& o);

	Datum& operator = (DatabaseObject& o)   { SetObject(o); return *this; }
	Datum& operator = (double d)            { SetNumber(d); return *this; }
	Datum& operator = (const string& s)     { SetString(s); return *this; }

	operator DatabaseObject& () const       { return GetObject(); }
	operator double () const                { return GetNumber(); }
	operator const string& () const         { return GetString(); }

private:
	void CheckType(int type) const;

private:
	scalar<int> _type;
	int _oid;
	double _number;
	string _string;
	set<int> _objectset;
};

#endif
