#ifndef DATABASE_H
#define DATABASE_H

class DatabaseObject;

class Database
{
public:
	Database();
	~Database();

	DatabaseObject& Get(int oid);
	DatabaseObject& Create();

private:
	iscalar<int, 0> _nextoid;

	typedef map<int, DatabaseObject*> DatabaseMap;
	DatabaseMap _map;
};

#endif
