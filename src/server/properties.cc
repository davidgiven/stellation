#include "globals.h"
#include "Database.h"
#include "Datum.h"
#include "SObject.h"
#include "Hash.h"
#include "Property.h"
#include "Writer.h"
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

Hash::Type Hash::GetFirstHash()
{
	int i = MIN_HASH_VALUE;
	while (!propertyNameTable[i])
		i++;
	return (Hash::Type) i;
}

bool Hash::GetNextHash(Hash::Type& hash)
{
	int i = (int) hash;

	if (i <= MAX_HASH_VALUE)
	{
		do
		{
			i++;
		}
		while ((i < MAX_HASH_VALUE) && !*propertyNameTable[i]);
	}

	hash = (Hash::Type) i;
	return (i <= MAX_HASH_VALUE);
}

namespace
{
	typedef map<Hash::Type, const Property&> PropertyDataMap;
	#include "property-tables.h"
}

const Property& GetPropertyInfo(Hash::Type name)
{
	PropertyDataMap::const_iterator i = propertyDataMap.find(name);
	assert(i != propertyDataMap.end());
	return i->second;
}

void WritePropertyInfo(Writer& writer)
{
	for (PropertyDataMap::const_iterator i = propertyDataMap.begin(),
			e = propertyDataMap.end(); i != e; i++)
	{
		Hash::Type kid = i->first;
		const Property& pinfo = i->second;

		writer.Write(kid);
		writer.Write(Hash::Class);
		writer.Write((Hash::Type) pinfo.type);
	}
}

void InitialiseClass(SObject& object)
{
	Hash::Type type = object.GetClass();
	*object.Class = type;
}
