#include "globals.h"
#include "Database.h"
#include "Datum.h"
#include "Property.h"
#include "Writer.h"
#include "Log.h"
#include "SObject.h"
#include "utils.h"
#include "okvstore.h"

static okvstore<Database::Type, Hash::Type, Datum> database;

Database::Type DatabaseAllocateOid()
{
	static unsigned int i = (int) Database::Universe;
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
	database.dirty(oid, kid);
}

void DatabaseCommit()
{
	int changed;
	database.diagnostics(changed);

//	Log() << "committing: "
//		  << changed << " changed values";
	database.commit();
}

void DatabaseRollback()
{
	int changed;
	database.diagnostics(changed);

	Log() << "rolling back: "
		  << changed << " changed values";
	database.rollback();

	SObject::FlushCache();
}

class WriterVisitor
{
	public:
		WriterVisitor(Writer& writer):
			_writer(writer)
		{ }

		void operator () (Database::Type oid, Hash::Type kid, const Datum& datum)
		{
			datum.Write(_writer);
		}

	private:
		Writer& _writer;
};

void DatabaseSave(Writer& writer)
{
	WriterVisitor visitor(writer);
	database.visit(visitor);
}

class ConditionalWriterVisitor
{
	public:
		ConditionalWriterVisitor(Writer& writer, bool owner, double lastUpdate):
			_writer(writer),
			_owner(owner),
			_lastUpdate(lastUpdate)
		{ }

		void operator () (Database::Type oid, Hash::Type kid, const Datum& datum)
		{
			if (datum.ChangedSince(_lastUpdate))
			{
				const Property& propertyInfo = GetPropertyInfo(kid);

				if ((propertyInfo.scope == Property::GLOBAL) ||
					(propertyInfo.scope == Property::LOCAL) ||
					((propertyInfo.scope == Property::PRIVATE) && _owner))
				{
					datum.Write(_writer);
				}
			}
		}

	private:
		Writer& _writer;
		bool _owner;
		double _lastUpdate;
};

void DatabaseWriteChangedDatums(Writer& writer,
		Database::Type oid, bool owner,
		double lastUpdate)
{
	ConditionalWriterVisitor visitor(writer, owner, lastUpdate);
	database.visit(visitor, oid);
}

