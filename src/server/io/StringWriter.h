#ifndef STRINGWRITER_H
#define STRINGWRITER_H

#include "Writer.h"
#include <sstream>

class StringWriter : public Writer
{
public:
	StringWriter():
		_empty(true)
	{
	}

	void Write(const string& s)
	{
		if (!_empty)
			_data << '\1';
		_data << s;
		_empty = false;
	}

	string GetData()
	{
		return _data.str();
	}

private:
	std::stringstream _data;
	scalar<bool> _empty;
};

#endif
