#include "globals.h"
#include "Database.h"
#include "Datum.h"
#include "Property.h"
#include "Writer.h"
#include "StringReader.h"
#include "StringWriter.h"
#include "Log.h"
#include "SObject.h"
#include "utils.h"
#include "SQL.h"

static map<Hash::Type, int> hashtodb;
static map<int, Hash::Type> dbtohash;

typedef std::pair<Database::Type, Hash::Type> CacheKey;
typedef shared_ptr<Datum> CacheValue;
typedef map<CacheKey, CacheValue> Cache;

static Cache datumcache;

static SQLSession* sql;
static unsigned databaseid;
static unsigned canonicalTime;

static void begin()
{
	static SQLStatement* s = new SQLStatement(sql, "BEGIN");
	s->Reset();
	s->Step();
}

static void commit()
{
	static SQLStatement* s = new SQLStatement(sql, "COMMIT");
	s->Reset();
	s->Step();
}

static void rollback()
{
	static SQLStatement* s = new SQLStatement(sql, "ROLLBACK");
	s->Reset();
	s->Step();
}

static void prepare()
{
	{
		static SQLStatement* s = new SQLStatement(sql,
				"SELECT id, value FROM 'tokens'");
		s->Reset();
		while (s->Step() == SQLITE_ROW)
		{
			int id = s->ColumnInt(0);
			string value = s->ColumnString(1);
			Hash::Type hash = Hash::ValidatedHashFromString(value);

			hashtodb[hash] = id;
			dbtohash[id] = hash;
		}
	}

	{
		static SQLStatement* s = new SQLStatement(sql,
				"SELECT MAX(oid) FROM 'eav'");
		s->Reset();
		s->Step();
		databaseid = s->ColumnUnsigned(0) + 1;
		Log() << "next database ID is " << databaseid;
	}

	{
		static SQLStatement* s = new SQLStatement(sql,
				"SELECT MAX(time) FROM 'eav'");
		s->Reset();
		s->Step();
		canonicalTime = s->ColumnUnsigned(0);
		if (canonicalTime == 0)
			canonicalTime = 1;
		Log() << "server canonical time is " << canonicalTime;
	}

	begin();
}

void DatabaseOpen(const string& filename)
{
	sql = new SQLSession(filename, SQLITE_OPEN_READWRITE);
	prepare();
}

void DatabaseCreate(const string& filename)
{
	if ((access(filename.c_str(), F_OK) == 0) ||
		(errno != ENOENT))
	{
		Error() << "file '" << filename << "' already exists";
	}

	sql = new SQLSession(filename, SQLITE_OPEN_READWRITE|SQLITE_OPEN_CREATE);

	extern unsigned int dbinit_length;
	extern const char dbinit_data[];
	string dbinit(dbinit_data, dbinit_length);
	sql->Exec(dbinit);

	begin();
	Hash::Type h = Hash::GetFirstHash();
	do
	{
		static SQLStatement* s = new SQLStatement(sql,
				"INSERT INTO 'tokens' VALUES (NULL, ?)");
		s->Reset();
		s->Bind(1, Hash::StringFromHash(h));
		s->Step();
	}
	while (Hash::GetNextHash(h));
	commit();

	prepare();
}

void DatabaseClose()
{
	rollback();
	delete sql;
}

Database::Type DatabaseAllocateOid()
{
	return (Database::Type) databaseid++;
}

shared_ptr<Datum>& DatabaseGet(Database::Type oid, Hash::Type kid)
{
	CacheKey key(oid, kid);
	shared_ptr<Datum>& d = datumcache[key];
	if (!d)
	{
		d.reset(Datum::Create(oid, kid));

		static SQLStatement* s = new SQLStatement(sql,
				"SELECT value, time FROM 'eav' WHERE oid=? AND kid=?");
		s->Reset();
		s->Bind(1, (int)oid);
		s->Bind(2, hashtodb[kid]);
		if (s->Step() == SQLITE_ROW)
		{
			string data = s->ColumnString(0);
			unsigned time = s->ColumnUnsigned(1);
			StringReader reader(data);
			d->Read(reader);
			d->SetLastChangeTime(time);
		}
	}
	return d;
}

bool DatabaseExists(Database::Type oid, Hash::Type kid)
{
	CacheKey key(oid, kid);
	if (datumcache.find(key) != datumcache.end())
		return true;

	static SQLStatement* s = new SQLStatement(sql,
			"SELECT COUNT(*) FROM 'eav' WHERE oid=? AND kid=?");
	s->Reset();
	s->Bind(1, (int)oid);
	s->Bind(2, hashtodb[kid]);
	s->Step();
	return !!s->ColumnInt(0);
}

void DatabaseDirty(Datum* datum)
{
	StringWriter writer;
	datum->Write(writer);

	static SQLStatement* s = new SQLStatement(sql,
			"INSERT OR REPLACE INTO 'eav' (oid, kid, value, time) "
				"VALUES (?, ?, ?, ?)");
	s->Reset();
	s->Bind(1, (int) datum->GetOid());
	s->Bind(2, hashtodb[datum->GetKid()]);
	s->Bind(3, writer.GetData());
	s->Bind(4, datum->LastChangeTime());

	if (s->Step() != SQLITE_DONE)
		Error() << "Unable to update database: "
				<< sql->GetError();
}

void DatabaseCommit()
{
	commit();
	begin();
}

void DatabaseRollback()
{
	Log() << "rolling back";

	SObject::FlushCache();
	datumcache.clear();
	LazyDatumBase::FlushCache();
	rollback();
	begin();
}

void DatabaseWriteChangedDatums(Writer& writer,
		Database::Type oid, bool owner,
		double lastUpdate)
{
	static SQLStatement* s = new SQLStatement(sql,
			"SELECT kid FROM 'eav' WHERE oid = ?");
	s->Reset();
	s->Bind(1, (int)oid);

	while (s->Step() == SQLITE_ROW)
	{
		Hash::Type kid = dbtohash[s->ColumnInt(0)];
		shared_ptr<Datum>& datum = DatabaseGet(oid, kid);

		if (datum->ChangedSince(lastUpdate))
		{
			const Property& propertyInfo = GetPropertyInfo(kid);

			if ((propertyInfo.scope == Property::GLOBAL) ||
				(propertyInfo.scope == Property::LOCAL) ||
				((propertyInfo.scope == Property::PRIVATE) && owner))
			{
				writer.Write(oid);
				writer.Write(kid);
				datum->Write(writer);
			}
		}
	}
}

void UpdateCanonicalTime()
{
	canonicalTime++;
}

unsigned CurrentCanonicalTime()
{
	return canonicalTime;
}

void RegisterPlayer(const string& email, Database::Type oid)
{
	static SQLStatement* s = new SQLStatement(sql,
			"INSERT INTO 'players' (email, oid) "
				"VALUES (?, ?)");
	s->Reset();
	s->Bind(1, email);
	s->Bind(2, (int)oid);
	if (s->Step() != SQLITE_DONE)
		Error() << "Unable to update database: "
				<< sql->GetError();
}

void UnregisterPlayer(const string& email)
{
	static SQLStatement* s = new SQLStatement(sql,
			"DROP FROM 'players' WHERE email = ?");
	s->Reset();
	s->Bind(1, email);
	if (s->Step() != SQLITE_DONE)
		Error() << "Unable to update database: "
				<< sql->GetError();
}

Database::Type FindPlayer(const string& email)
{
	static SQLStatement* s = new SQLStatement(sql,
			"SELECT oid FROM 'players' WHERE email = ?");
	s->Reset();
	s->Bind(1, email);
	if (s->Step() == SQLITE_ROW)
		return (Database::Type) s->ColumnInt(0);
	return Database::Null;
}
