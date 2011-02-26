#ifndef HASH_H
#define HASH_H

class Hash : private noncopyable
{
public:
	enum Type
	{
#include "hash-enum.h"
	};

	static Type ValidatedHashFromString(const char* s, int length);
	static Type ValidatedHashFromString(const string& s);
	static Type HashFromString(const char* s, int length);
	static Type HashFromString(const string& s);
	static const char* StringFromHash(Type hash);
};

#endif
