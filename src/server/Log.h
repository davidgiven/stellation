#ifndef LOG_H
#define LOG_H

#include "Hash.h"
#include "Database.h"

class SObject;

class Error
{
public:
	Error();
	~Error();

	template <class T> Error& operator << (T& t)
	{
		const string& s = t;
		return (*this << s);
	}

	Error& operator << (const string& t);
};

class Log
{
public:
	Log();
	~Log();

	template <class T> Log& operator << (T& t)
	{
		const string& s = t;
		return (*this << s);
	}

	Log& operator << (Hash::Type t);
	Log& operator << (Database::Type o);
	Log& operator << (int i);
	Log& operator << (const string& t);
};

#endif
