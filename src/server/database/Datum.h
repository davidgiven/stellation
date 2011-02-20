#ifndef DATUM_H
#define DATUM_H

class Datum
{
public:
	Datum();

	enum
	{
		UNSET,
		NUMBER,
		STRING,
		SET,
		LIST
	};

	int GetType() const
	{ return _type; }

	void SetType(int type);

	double GetNumber() const;
	void SetNumber(double d);

	const string& GetString() const;
	void SetString(const string& s);

	Datum& operator = (double d)            { SetNumber(d); return *this; }
	Datum& operator = (const string& s)     { SetString(s); return *this; }

	operator double () const                { return GetNumber(); }
	operator const string& () const         { return GetString(); }

private:
	void CheckType(int type) const;

private:
	scalar<int> _type;
	double _number;
	string _string;
};

#endif
