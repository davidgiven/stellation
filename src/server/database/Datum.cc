#include "globals.h"
#include "Database.h"
#include "Datum.h"
#include "Property.h"
#include "SObject.h"
#include "Reader.h"
#include "Writer.h"
#include "utils.h"
#include "mainloop.h"
#include <boost/variant.hpp>
#include <algorithm>

Datum* Datum::Create(Database::Type oid, Hash::Type kid)
{
	const Property& propertyInfo = GetPropertyInfo(kid);
	switch (propertyInfo.type)
	{
		case Datum::ObjectDatumType:        return new ObjectDatum(oid, kid);
		case Datum::TokenDatumType:         return new TokenDatum(oid, kid);
		case Datum::NumberDatumType:        return new NumberDatum(oid, kid);
		case Datum::StringDatumType:        return new StringDatum(oid, kid);
		case Datum::ObjectSetDatumType:     return new ObjectSetDatum(oid, kid);
		case Datum::ObjectMapDatumType:     return new ObjectMapDatum(oid, kid);

		default:
			assert(false);
	}
}

Datum::Datum(Database::Type oid, Hash::Type kid):
	_oid(oid),
	_kid(kid),
	_lastChanged(CurrentCanonicalTime())
{
}

Datum::~Datum()
{
}

void Datum::Dirty()
{
	DatabaseDirty(this);
	_lastChanged = CurrentCanonicalTime();
}

void NumberDatum::SetNumber(double d)
{
	_value = d;
	Dirty();
}

void NumberDatum::Write(Writer& writer) const
{
	writer.Write(_value);
}

void NumberDatum::Read(Reader& reader)
{
	_value = reader.ReadNumber();
}

void StringDatum::SetString(const string& s)
{
	_value = s;
	Dirty();
}

void StringDatum::Write(Writer& writer) const
{
	writer.Write(_value);
}

void StringDatum::Read(Reader& reader)
{
	_value = reader.ReadString();
}

void ObjectDatum::SetObject(SObject* o)
{
	SetObject(*o);
}

void ObjectDatum::SetObject(Database::Type oid)
{
	_value = oid;
	Dirty();
}

void ObjectDatum::Write(Writer& writer) const
{
	writer.Write(_value);
}

void ObjectDatum::Read(Reader& reader)
{
	_value = reader.ReadOid();
}

void TokenDatum::SetToken(Hash::Type token)
{
	_value = token;
	Dirty();
}

void TokenDatum::Write(Writer& writer) const
{
	writer.Write(_value);
}

void TokenDatum::Read(Reader& reader)
{
	_value = reader.ReadHash();
}

void ObjectSetDatum::AddToSet(SObject* o)
{
	AddToSet(*o);
}

void ObjectSetDatum::AddToSet(Database::Type oid)
{
	_value.insert(oid);
	Dirty();
}

void ObjectSetDatum::RemoveFromSet(SObject* o)
{
	RemoveFromSet(*o);
}

void ObjectSetDatum::RemoveFromSet(Database::Type oid)
{
	_value.erase(oid);
	Dirty();
}

bool ObjectSetDatum::InSet(SObject* o)
{
	return InSet(*o);
}

bool ObjectSetDatum::InSet(Database::Type oid)
{
	return (_value.find(oid) != _value.end());
}

void ObjectSetDatum::ClearSet()
{
	_value.clear();
	Dirty();
}

int ObjectSetDatum::SetLength() const
{
	return _value.size();
}

Database::Type ObjectSetDatum::RandomSetMember() const
{
	ObjectSet::const_iterator i = _value.begin();
	std::advance(i, Random(_value.size()));
	return *i;
}

Datum::ObjectSet::const_iterator ObjectSetDatum::SetBegin() const
{
	return _value.begin();
}

Datum::ObjectSet::const_iterator ObjectSetDatum::SetEnd() const
{
	return _value.end();
}

void ObjectSetDatum::Write(Writer& writer) const
{
	writer.Write(_value.size());

	for (Datum::ObjectSet::const_iterator i = _value.begin(),
			e = _value.end(); i != e; i++)
	{
		writer.Write(*i);
	}
}

void ObjectSetDatum::Read(Reader& reader)
{
	_value.clear();
	int count = reader.ReadNumber();

	for (int i=0; i<count; i++)
	{
		Database::Type oid = reader.ReadOid();
		_value.insert(oid);
	}
}

void ObjectMapDatum::AddToMap(const string& key, SObject* o)
{
	AddToMap(key, *o);
}

void ObjectMapDatum::AddToMap(const string& key, Database::Type oid)
{
	_value[key] = oid;
	Dirty();
}

void ObjectMapDatum::RemoveFromMap(const string& key)
{
	_value.erase(key);
	Dirty();
}

Database::Type ObjectMapDatum::FetchFromMap(const string& key)
{
	ObjectMap::const_iterator i = _value.find(key);
	if (i == _value.end())
		return Database::Null;
	return i->second;
}

int ObjectMapDatum::MapLength() const
{
	return _value.size();
}

Datum::ObjectMap::const_iterator ObjectMapDatum::MapBegin() const
{
	return _value.begin();
}

Datum::ObjectMap::const_iterator ObjectMapDatum::MapEnd() const
{
	return _value.end();
}

void ObjectMapDatum::Write(Writer& writer) const
{
	writer.Write(_value.size());

	for (Datum::ObjectMap::const_iterator i = _value.begin(),
			e = _value.end(); i != e; i++)
	{
		writer.Write(i->first);
		writer.Write(i->second);
	}
}

void ObjectMapDatum::Read(Reader& reader)
{
	int size = reader.ReadNumber();

	_value.clear();
	for (int i = 0; i < size; i++)
	{
		string key = reader.ReadString();
		Database::Type oid = reader.ReadOid();
		_value[key] = oid;
	}
}

#if 0
void Datum::Write(Writer& writer) const
{
	writer.Write(_oid);
	writer.Write(_kid);

	switch ((Datum::Type) _type)
	{
		case Datum::NUMBER:
			writer.Write(Hash::Number);
			writer.Write(GetNumber());
			break;

		case Datum::STRING:
		{
			writer.Write(Hash::String);
			writer.Write(GetString());
			break;
		}

		case Datum::OBJECT:
		{
			writer.Write(Hash::Object);
			writer.Write(GetObject());
			break;
		}

		case Datum::TOKEN:
		{
			writer.Write(Hash::Token);
			writer.Write(GetToken());
			break;
		}

		case Datum::OBJECTSET:
		{
			const ObjectSet& os = boost::get<ObjectSet>(_value);

			writer.Write(Hash::ObjectSet);
			writer.Write(os.size());

			for (Datum::ObjectSet::const_iterator i = os.begin(),
					e = os.end(); i != e; i++)
			{
				writer.Write(*i);
			}

			break;
		}

		case Datum::OBJECTMAP:
		{
			const ObjectMap& om = boost::get<ObjectMap>(_value);

			writer.Write(Hash::ObjectMap);
			writer.Write(om.size());

			for (Datum::ObjectMap::const_iterator i = om.begin(),
					e = om.end(); i != e; i++)
			{
				writer.Write(i->first);
				writer.Write(i->second);
			}

			break;
		}

		default:
			assert(false);
	}
}
#endif
