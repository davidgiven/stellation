#ifndef PROPERTY_H
#define PROPERTY_H

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

class SObject;
extern void InitialiseClass(SObject& object);

#endif
