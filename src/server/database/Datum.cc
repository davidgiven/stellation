#include "globals.h"
#include "Database.h"
#include "DatabaseObject.h"
#include "Datum.h"

Datum::Datum():
	_type(UNSET),
	_scope(PRIVATE)
{
}

void Datum::CheckType(Type type) const
{
	assert(_type == type);
}

void Datum::SetType(Type type)
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

void Datum::SetObject(DatabaseObject& dbo)
{
	CheckType(OBJECT);
	_oid = dbo;
}

DatabaseObject& Datum::GetObject() const
{
	CheckType(OBJECT);
	return DBGet(_oid);
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

int Datum::SetLength() const
{
	CheckType(OBJECTSET);
	return _objectset.size();
}

Datum::ObjectSet::const_iterator Datum::SetBegin() const
{
	CheckType(OBJECTSET);
	return _objectset.begin();
}

Datum::ObjectSet::const_iterator Datum::SetEnd() const
{
	CheckType(OBJECTSET);
	return _objectset.end();
}
