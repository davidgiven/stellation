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

class SObject;
extern void InitialiseClass(SObject& object);

#endif
