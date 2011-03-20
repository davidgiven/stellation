#ifndef STRINGREADER_H
#define STRINGREADER_H

#include "Reader.h"
#include <sstream>

class StringReader : public Reader
{
public:
	StringReader(const string& data)
	{
		std::stringstream ss(data);

		string item;
		while (std::getline(ss, item, '\1'))
			_values.push_back(item);

		_iterator = _values.begin();
	}

	string ReadString()
	{
		return *_iterator++;
	}

	bool IsEOF() const
	{
		return _iterator == _values.end();
	}

private:
	std::list<string> _values;
	std::list<string>::const_iterator _iterator;
};

#endif
