#ifndef READER_H
#define READER_H

#include "Hash.h"

class Reader
{
public:
	virtual ~Reader() {};
	virtual string ReadString() = 0;
	virtual bool IsEOF() const = 0;

	double ReadNumber();
	Hash::Type ReadHash();
};

#endif
