#ifndef READER_H
#define READER_H

class Reader
{
public:
	virtual ~Reader() {};
	virtual void Consume() = 0;
	virtual Hash::Type ReadHash() = 0;
	virtual string ReadString() = 0;
	virtual bool IsEOF() const = 0;
};

#endif
