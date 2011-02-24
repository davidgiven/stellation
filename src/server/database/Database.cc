#include "globals.h"
#include "Database.h"
#include "DatabaseObject.h"
#include "Property.h"
#include "Writer.h"

Database& Database::GetInstance()
{
	static Database instance;
	return instance;
}

Database::Database()
{
}

Database::~Database()
{
}

DatabaseObject& Database::Create()
{
	int oid = _nextoid++;

	shared_ptr<DatabaseObject> o(new DatabaseObject(oid));
	_map[oid] = o;

	return *o;
}

DatabaseObject& Database::Get(int oid)
{
	DatabaseMap::const_iterator i = _map.find(oid);
	assert(i != _map.end());

	return *(i->second);
}

void Database::Save(Writer& stream)
{
	stream.Write((int)_map.size());

	for (DatabaseMap::const_iterator i = _map.begin(),
			e = _map.end(); i != e; i++)
	{
		DatabaseObject& dbo = *(i->second);

		stream.Write((int)dbo);

		for (DatabaseObject::Map::const_iterator i = dbo.Begin(),
				e = dbo.End(); i != e; i++)
		{
			Hash::Type hash = i->first;
			Datum& datum = *(i->second);

			stream.Write(hash);

			switch (datum.GetType())
			{
				case Datum::NUMBER:
					stream.Write(Hash::Number);
					stream.Write((double)datum);
					break;

				case Datum::STRING:
				{
					stream.Write(Hash::String);
					stream.Write((string)datum);
					break;
				}

				case Datum::OBJECT:
				{
					stream.Write(Hash::Object);
					stream.Write(datum.GetObjectAsOid());
					break;
				}

				case Datum::TOKEN:
				{
					stream.Write(Hash::Token);
					stream.Write((Hash::Type)datum);
					break;
				}

				case Datum::OBJECTSET:
				{
					stream.Write(Hash::ObjectSet);
					stream.Write(datum.SetLength());

					for (Datum::ObjectSet::const_iterator i = datum.SetBegin(),
							e = datum.SetEnd(); i != e; i++)
					{
						stream.Write(*i);
					}

					break;
				}

				default:
					assert(false);
			}
		}
	}
}
