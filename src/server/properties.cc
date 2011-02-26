#include "globals.h"
#include "Database.h"
#include "Datum.h"
#include "SObject.h"
#include "Hash.h"
#include "Property.h"
#include <boost/range/size.hpp>
#include <vector>

#include "hash.h"

Hash::Type Hash::ValidatedHashFromString(const char* data, int length)
{
	string s(data, length);
	Hash::Type hash = HashFromString(s);
	if (((int)hash >= MIN_HASH_VALUE) && ((int)hash <= MAX_HASH_VALUE) &&
		(s == propertyNameTable[hash]))
	{
		return hash;
	}

	return Hash::Null;
}

Hash::Type Hash::ValidatedHashFromString(const string& s)
{
	return ValidatedHashFromString(s.data(), s.size());
}

Hash::Type Hash::HashFromString(const char* s, int length)
{
	return (Hash::Type) propertyHash(s, length);
}

Hash::Type Hash::HashFromString(const string& s)
{
	return (Hash::Type) propertyHash(s.data(), s.size());
}

const char* Hash::StringFromHash(Hash::Type hash)
{
	int i = (int) hash;

	assert((i >= MIN_HASH_VALUE) && (i <= MAX_HASH_VALUE));
	return propertyNameTable[i];
}

namespace
{
	struct ClassInitData
	{
		const ClassInitData* superclass;
		int numproperties;
		const Hash::Type* properties;
	};

	typedef map<Hash::Type, const ClassInitData&> ClassInitialisationMap;
	typedef map<Hash::Type, const Property&> PropertyDataMap;
	#include "property-tables.h"
}

const Property& GetPropertyInfo(Hash::Type name)
{
	PropertyDataMap::const_iterator i = propertyDataMap.find(name);
	assert(i != propertyDataMap.end());
	return i->second;
}

void InitialiseClass(SObject& object)
{
	Hash::Type type = object.GetClass();

	assert(classInitialisationMap.find(type) != classInitialisationMap.end());
	const ClassInitData* cid = &(classInitialisationMap.find(type)->second);
	while (cid)
	{
		for (int i = 0; i < cid->numproperties; i++)
		{
			Hash::Type hash = cid->properties[i];
			const Property& data = propertyDataMap.find(hash)->second;

			Datum& datum = object.Get(hash);
			datum.SetType(data.type);
		}

		cid = cid->superclass;
	}

	object.Class = type;
}
