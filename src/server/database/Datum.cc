#include "globals.h"
#include "DatabaseObject.h"
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

void Datum::AddToSet(DatabaseObject& dbo)
{
	CheckType(OBJECTSET);
	_objectset.insert(dbo);
}

void Datum::RemoveFromSet(DatabaseObject& dbo)
{
	CheckType(OBJECTSET);
	_objectset.erase(dbo);
}

bool Datum::InSet(DatabaseObject& dbo)
{
	CheckType(OBJECTSET);
	return (_objectset.find(dbo) != _objectset.end());
}
