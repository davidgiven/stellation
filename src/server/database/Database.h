#ifndef DATABASE_H
#define DATABASE_H

class DatabaseObject;

class Database : public noncopyable
{
public:
	static Database& GetInstance();

public:
	Database();
	~Database();

	DatabaseObject& Get(int oid);
	DatabaseObject& Create();

	void Save(std::ostream& stream);

private:
	iscalar<int, 1> _nextoid;

	typedef map<int, shared_ptr<DatabaseObject> > DatabaseMap;
	DatabaseMap _map;
};

static inline DatabaseObject& DBGet(int oid)
{
	return Database::GetInstance().Get(oid);
}

static inline DatabaseObject& DBCreate()
{
	return Database::GetInstance().Create();
}

#endif
