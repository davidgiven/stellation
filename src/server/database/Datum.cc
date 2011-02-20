#include "globals.h"
#include "Datum.h"

Datum::Datum():
	_type(UNSET)
{
}

void Datum::CheckType(int type) const
{
	assert(_type == type);
}

void Datum::SetType(int type)
{
	assert(_type == UNSET);
	_type = type;
}

void Datum::SetNumber(double d)
{
	CheckType(NUMBER);
	_number = d;
}

double Datum::GetNumber() const
{
	CheckType(NUMBER);
	return _number;
}

void Datum::SetString(const string& s)
{
	CheckType(STRING);
	_string = s;
}

const string& Datum::GetString() const
{
	CheckType(STRING);
	return _string;
}

