#include "globals.h"
#include "Database.h"
#include "DatabaseObject.h"

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
