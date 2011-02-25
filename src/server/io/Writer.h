#ifndef WRITER_H
#define WRITER_H

class Writer
{
public:
	virtual ~Writer() {};
	virtual void Write(const string& s) = 0;
	void Write(Database::Type i);
	void Write(Hash::Type t);
	void Write(int i);
	void Write(double d);
};

#endif
