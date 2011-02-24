#ifndef WRITER_H
#define WRITER_H

class Writer
{
public:
	virtual ~Writer() {};
	virtual void Write(const string& s) = 0;
	void Write(int i);
	void Write(double d);
	void Write(Hash::Type t);
};

#endif
