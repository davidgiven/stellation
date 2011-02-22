#ifndef HASH_H
#define HASH_H

class Hash : private noncopyable
{
public:
	typedef struct opaque* Type;

	static Type HashFromString(const char* s, int length);
	static Type HashFromString(const string& s);
	static const char* StringFromHash(Type hash);

#include "token-accessors-h.h"
};

#endif
