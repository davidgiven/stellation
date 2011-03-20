#ifndef PROPERTY_H
#define PROPERTY_H

#include "Datum.h"

class Writer;

struct Property
{
	enum Type
	{
		GLOBAL,
		LOCAL,
		PRIVATE,
		SERVERONLY,
		MEMORYONLY
	};

	Datum::Type type;
	Type scope;
};

extern const Property& GetPropertyInfo(Hash::Type name);
extern void WritePropertyInfo(Writer& writer);

class SObject;
extern void InitialiseClass(SObject& object);

#endif
