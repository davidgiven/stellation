#ifndef SQL_H
#define SQL_H

#include <sqlite3.h>

class SQLStatement;

class SQLSession : public noncopyable
{
public:
	SQLSession(const string& filename, int flags)
	{
		int e = sqlite3_open_v2(filename.c_str(), &_dbhandle, flags, NULL);
		if (e != SQLITE_OK)
		{
			Error() << "unable to open database: "
					<< GetError();
		}
	}

	~SQLSession();

	sqlite3* GetHandle() const
	{
		return _dbhandle;
	}

	string GetError()
	{
		return sqlite3_errmsg(_dbhandle);
	}

	void Exec(const string& sql)
	{
		int e = sqlite3_exec(_dbhandle, sql.c_str(), NULL, NULL, NULL);
		if (e != SQLITE_OK)
		{
			Error() << "SQL execution error: "
					<< GetError();
		}
	}

	void RegisterStatement(SQLStatement* statement)
	{
		_statements.insert(statement);
	}

	void UnregisterStatement(SQLStatement* statement)
	{
		_statements.erase(statement);
	}

private:
	sqlite3* _dbhandle;
	set<SQLStatement*> _statements;
};

class SQLStatement : public noncopyable
{
public:
	SQLStatement(SQLSession* session, const string& sql):
		_session(session)
	{
		int e = sqlite3_prepare_v2(session->GetHandle(),
				sql.data(), sql.size(),
				&_statement, NULL);
		if (e != SQLITE_OK)
		{
			Error() << "failed to compile SQL statement '"
					<< sql << "': "
					<< session->GetError();
		}

		session->RegisterStatement(this);
	}

	~SQLStatement()
	{
		sqlite3_finalize(_statement);
		_session->UnregisterStatement(this);
	}

	void Bind(int n, int value)
	{
		sqlite3_bind_int(_statement, n, value);
	}

	void Bind(int n, unsigned value)
	{
		sqlite3_bind_int64(_statement, n, value);
	}

	void Bind(int n, double value)
	{
		sqlite3_bind_double(_statement, n, value);
	}

	void Bind(int n, const string& value)
	{
		sqlite3_bind_text(_statement, n, value.data(), value.size(),
				SQLITE_TRANSIENT);
	}

	void Reset()
	{
		sqlite3_reset(_statement);
	}

	int Step()
	{
		return sqlite3_step(_statement);
	}

	int ColumnInt(int n)
	{
		return sqlite3_column_int(_statement, n);
	}

	int ColumnUnsigned(int n)
	{
		return sqlite3_column_int64(_statement, n);
	}

	double ColumnDouble(int n)
	{
		return sqlite3_column_double(_statement, n);
	}

	string ColumnString(int n)
	{
		int length = sqlite3_column_bytes(_statement, n);
		const unsigned char* data = sqlite3_column_text(_statement, n);
		return string((const char*)data, length);
	}

private:
	SQLSession* _session;
	sqlite3_stmt* _statement;
};

inline SQLSession::~SQLSession()
{
	while (!_statements.empty())
	{
		SQLStatement* statement = *_statements.begin();
		delete statement;
	}

	int e = sqlite3_close(_dbhandle);
	if (e != SQLITE_OK)
	{
		Error() << "failed to close database: "
				<< sqlite3_errmsg(_dbhandle);
	}
}

#endif
