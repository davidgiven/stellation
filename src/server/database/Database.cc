#include "globals.h"
#include "Database.h"
#include "DatabaseObject.h"
#include "Property.h"

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

void Database::Save(std::ostream& stream)
{
	stream << "Stellation 3 Dump File" << std::endl
	       << "Version X.X" << std::endl;

	for (DatabaseMap::const_iterator i = _map.begin(),
			e = _map.end(); i != e; i++)
	{
		DatabaseObject& dbo = *(i->second);

		stream << (int)dbo << std::endl;

		for (DatabaseObject::Map::const_iterator i = dbo.Begin(),
				e = dbo.End(); i != e; i++)
		{
			Hash::Type hash = i->first;
			Datum& datum = *(i->second);

			stream << Hash::StringFromHash(hash) << std::endl;

			switch (datum.GetType())
			{
				case Datum::NUMBER:
					stream << 'n' << (double)datum << std::endl;
					break;

				case Datum::STRING:
				{
					const string& s = datum;
					stream << 's' << s.size() << std::endl;
					stream << s << std::endl;

					break;
				}

				case Datum::OBJECT:
				{
					stream << 'o' << datum.GetObjectAsOid() << std::endl;
					break;
				}

				case Datum::TOKEN:
				{
					stream << 't' << Hash::StringFromHash(datum) << std::endl;
					break;
				}

				case Datum::OBJECTSET:
				{
					stream << 'O' << datum.SetLength() << std::endl;
					for (Datum::ObjectSet::const_iterator i = datum.SetBegin(),
							e = datum.SetEnd(); i != e; i++)
					{
						stream << *i << std::endl;
					}

					break;
				}

				default:
					assert(false);
			}
		}

		stream << "." << std::endl;
	}

	stream << "." << std::endl;
}
