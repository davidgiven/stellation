#ifndef DATABASE_H
#define DATABASE_H

#include "Hash.h"
class DatabaseObject;
class Writer;
class Datum;

namespace Database
{
	typedef struct opaque* Type;
	const Type Null = NULL;
	const Type Universe = (Type) 1;
}

extern Datum& DatabaseGet(Database::Type oid, Hash::Type kid);
extern void DatabaseDirty(Database::Type oid, Hash::Type kid);
extern void DatabaseCommit();
extern void DatabaseRollback();
extern double DatabaseLastChangedTime(Database::Type oid);

extern Database::Type DatabaseAllocateOid();
extern void DatabaseSave(Writer& writer);

#endif

