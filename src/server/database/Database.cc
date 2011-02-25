#include "globals.h"
#include "Database.h"
#include "Datum.h"
#include "Property.h"
#include "Writer.h"
#include "Log.h"
#include "utils.h"
#include "okvstore.h"

static okvstore<Database::Type, Hash::Type, Datum> database;
static map<Database::Type, double> lastChanged;

Database::Type DatabaseAllocateOid()
{
	static unsigned int i = 1;
	return (Database::Type) i++;
}

Datum& DatabaseGet(Database::Type oid, Hash::Type kid)
{
	Datum& datum = database.get(oid, kid);
	if (datum.GetOid() == Database::Null)
	{
		/* New, freshly-minted datum --- initialise it. */
		datum.SetOidKid(oid, kid);
	}
	return datum;
}

void DatabaseDirty(Database::Type oid, Hash::Type kid)
{
	lastChanged[oid] = CurrentTime();
	database.dirty(oid, kid);
}

void DatabaseCommit()
{
	int changed;
	database.diagnostics(changed);

	Log() << "committing: "
		  << changed << " changed values";
	database.commit();
}

void DatabaseRollback()
{
	int changed;
	database.diagnostics(changed);

	Log() << "rolling back: "
		  << changed << " changed values";
	database.rollback();
}

double DatabaseLastChangedTime(Database::Type oid)
{
	return lastChanged[oid];
}

class WriterVisitor
{
	public:
		WriterVisitor(Writer& writer):
			_writer(writer)
		{ }

		void operator () (Database::Type oid, Hash::Type kid, const Datum& datum)
		{
			_writer.Write(oid);
			_writer.Write(kid);

			switch (datum.GetType())
			{
				case Datum::NUMBER:
					_writer.Write(Hash::Number);
					_writer.Write((double)datum);
					break;

				case Datum::STRING:
				{
					_writer.Write(Hash::String);
					_writer.Write((string)datum);
					break;
				}

				case Datum::OBJECT:
				{
					_writer.Write(Hash::Object);
					_writer.Write(datum.GetObject());
					break;
				}

				case Datum::TOKEN:
				{
					_writer.Write(Hash::Token);
					_writer.Write((Hash::Type)datum);
					break;
				}

				case Datum::OBJECTSET:
				{
					_writer.Write(Hash::ObjectSet);
					_writer.Write(datum.SetLength());

					for (Datum::ObjectSet::const_iterator i = datum.SetBegin(),
							e = datum.SetEnd(); i != e; i++)
					{
						_writer.Write(*i);
					}

					break;
				}

				default:
					assert(false);
			}
		}

	private:
		Writer& _writer;
};

void DatabaseSave(Writer& writer)
{
	WriterVisitor visitor(writer);
	database.visit(visitor);
}

#if 0
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

int Database::AllocateOid()
{
	return _nextoid++;
}

Datum& Database::Get(int oid, Hash::Type key)
{
	shared_ptr<Datum> ptr = _map[oid][key];
	if (!ptr)
		ptr = new Datum(oid, key);
	return *(ptr.get());
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

		}
	}
}
#endif
