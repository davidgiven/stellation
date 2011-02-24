#ifndef LOG_H
#define LOG_H

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

	Log& operator << (const string& t);
};

#endif
