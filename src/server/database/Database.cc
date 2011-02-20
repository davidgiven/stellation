#include "globals.h"
#include "Database.h"
#include "DatabaseObject.h"

Database::Database()
{
}

Database::~Database()
{
}

DatabaseObject& Database::Create()
{
	int oid = _nextoid++;

	auto_ptr<DatabaseObject> o(new DatabaseObject(oid));
	DatabaseObject* op = o.get();
	_map[oid] = op;
	o.release();

	return *op;
}
