#include "globals.h"
#include "Database.h"
#include "Datum.h"
#include <boost/variant.hpp>

Datum::Datum():
	_type(UNSET),
	_oid(Database::Null),
	_kid(Hash::Null)
{
}

Datum::Datum(Database::Type oid, Hash::Type kid):
	_type(UNSET),
	_oid(oid),
	_kid(kid)
{
}

Datum::Datum(const Datum& other):
	_type(other._type),
	_oid(other._oid),
	_kid(other._kid),
	_value(other._value)
{
}

Datum& Datum::operator = (const Datum& other)
{
	_type = other._type;
	_oid = other._oid;
	_kid = other._kid;
	_value = other._value;
}

void Datum::SetOidKid(Database::Type oid, Hash::Type kid)
{
	_oid = oid;
	_kid = kid;
}

void Datum::CheckType(Type type) const
{
	assert(_type == type);
}

void Datum::SetType(Type type)
{
	assert(_type == UNSET);
	_type = type;

	switch (type)
	{
		case TOKEN:
			_value = Hash::Null;
			break;

		case OBJECT:
			_value = Database::Null;
			break;

		case NUMBER:
			_value = 0.0;
			break;

		case STRING:
			_value = "";
			break;

		case OBJECTSET:
		{
			ObjectSet empty;
			_value = empty;
			break;
		}

		case OBJECTMAP:
		{
			ObjectMap empty;
			_value = empty;
			break;
		}

		case UNSET:
			assert(false);
	}
}

void Datum::Dirty()
{
	DatabaseDirty(_oid, _kid);
}

void Datum::SetNumber(double d)
{
	CheckType(NUMBER);
	Dirty();
	_value = d;
}

double Datum::GetNumber() const
{
	CheckType(NUMBER);
	return boost::get<double>(_value);
}

void Datum::SetString(const string& s)
{
	CheckType(STRING);
	Dirty();
	_value = s;
}

const string& Datum::GetString() const
{
	CheckType(STRING);
	return boost::get<string>(_value);
}

void Datum::SetObject(Database::Type oid)
{
	CheckType(OBJECT);
	Dirty();
	_value = oid;
}

Database::Type Datum::GetObject() const
{
	CheckType(OBJECT);
	return boost::get<Database::Type>(_value);
}

void Datum::SetToken(Hash::Type token)
{
	CheckType(TOKEN);
	Dirty();
	_value = token;
}

Hash::Type Datum::GetToken() const
{
	CheckType(TOKEN);
	return boost::get<Hash::Type>(_value);
}

void Datum::AddToSet(Database::Type oid)
{
	CheckType(OBJECTSET);
	Dirty();

	ObjectSet& os = boost::get<ObjectSet>(_value);
	os.insert(oid);
}

void Datum::RemoveFromSet(Database::Type oid)
{
	CheckType(OBJECTSET);
	Dirty();

	ObjectSet& os = boost::get<ObjectSet>(_value);
	os.erase(oid);
}

bool Datum::InSet(Database::Type oid)
{
	CheckType(OBJECTSET);

	ObjectSet& os = boost::get<ObjectSet>(_value);
	return (os.find(oid) != os.end());
}

int Datum::SetLength() const
{
	CheckType(OBJECTSET);

	const ObjectSet& os = boost::get<ObjectSet>(_value);
	return os.size();
}

Datum::ObjectSet::const_iterator Datum::SetBegin() const
{
	CheckType(OBJECTSET);

	const ObjectSet& os = boost::get<ObjectSet>(_value);
	return os.begin();
}

Datum::ObjectSet::const_iterator Datum::SetEnd() const
{
	CheckType(OBJECTSET);

	const ObjectSet& os = boost::get<ObjectSet>(_value);
	return os.end();
}

void Datum::AddToMap(const string& key, Database::Type oid)
{
	CheckType(OBJECTMAP);
	Dirty();

	ObjectMap& om = boost::get<ObjectMap>(_value);
	om[key] = oid;
}

void Datum::RemoveFromMap(const string& key)
{
	CheckType(OBJECTMAP);
	Dirty();

	ObjectMap& om = boost::get<ObjectMap>(_value);
	om.erase(key);
}

Database::Type Datum::FetchFromMap(const string& key)
{
	CheckType(OBJECTMAP);

	ObjectMap& om = boost::get<ObjectMap>(_value);
	ObjectMap::const_iterator i = om.find(key);
	if (i == om.end())
		return Database::Null;
	return i->second;
}

int Datum::MapLength() const
{
	CheckType(OBJECTMAP);

	const ObjectMap& om = boost::get<ObjectMap>(_value);
	return om.size();
}

Datum::ObjectMap::const_iterator Datum::MapBegin() const
{
	CheckType(OBJECTMAP);

	const ObjectMap& om = boost::get<ObjectMap>(_value);
	return om.begin();
}

Datum::ObjectMap::const_iterator Datum::MapEnd() const
{
	CheckType(OBJECTMAP);

	const ObjectMap& om = boost::get<ObjectMap>(_value);
	return om.end();
}
