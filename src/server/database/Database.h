#ifndef DATABASE_H
#define DATABASE_H

#include "Hash.h"
class DatabaseObject;
class Writer;
class Datum;
class ObjectSet;

namespace Database
{
	typedef struct opaque* Type;
	const Type Null = NULL;
	const Type Universe = (Type) 1;

	enum Visibility
	{
		GlobalVisibility,
		LocalVisibility,
		OwnerVisibility
	};
}

extern void DatabaseOpen(const string& filename);
extern void DatabaseCreate(const string& filename);
extern void DatabaseClose();

extern boost::shared_ptr<Datum>& DatabaseGet(Database::Type oid, Hash::Type kid);
extern bool DatabaseExists(Database::Type oid, Hash::Type kid);
extern void DatabaseDirty(Datum* datum);
extern void DatabaseCommit();
extern void DatabaseRollback();

extern Database::Type DatabaseAllocateOid();

extern void UpdateCanonicalTime();
extern unsigned CurrentCanonicalTime();

extern void RegisterPlayer(const string& email, Database::Type oid);
extern void UnregisterPlayer(const string& email);
extern Database::Type FindPlayer(const string& email);

extern void DatabaseWriteChangedDatums(Writer& writer,
		Database::Type oid, Database::Visibility visibility,
		double lastUpdate);

#endif

