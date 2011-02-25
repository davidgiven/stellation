#ifndef LOG_H
#define LOG_H

#include "Hash.h"
#include "Database.h"

class Error
{
public:
	Error();
	~Error();

	template <class T> Error& operator << (const T& t)
	{
		return (*this << (string)t);
	}

	Error& operator << (const string& t);
};

class Log
{
public:
	Log();
	~Log();

	template <class T> Log& operator << (const T& t)
	{
		return (*this << (string)t);
	}

	Log& operator << (Hash::Type t);
	Log& operator << (Database::Type o);
	Log& operator << (int i);
	Log& operator << (const string& t);
};

#endif
