#ifndef LOG_H
#define LOG_H

#include "Hash.h"
#include "Database.h"

class SObject;
class Datum;
template <class T> class LazyDatum;

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

	Log& operator << (Hash::Type t);
	Log& operator << (SObject* o);
	Log& operator << (Database::Type o);
	Log& operator << (double d);
	Log& operator << (const string& t);
};

#endif
