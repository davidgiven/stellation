#include "globals.h"
#include "Database.h"
#include "Reader.h"
#include "Writer.h"
#include <boost/lexical_cast.hpp>

Hash::Type Reader::ReadHash()
{
	string s = ReadString();
	return Hash::ValidatedHashFromString(s);
}

double Reader::ReadNumber()
{
	string s = ReadString();

	try
	{
		return boost::lexical_cast<double>(s);
	}
	catch (boost::bad_lexical_cast e)
	{
		throw Hash::MalformedCommand;
	}
}

Database::Type Reader::ReadOid()
{
	string s = ReadString();

	try
	{
		return (Database::Type) boost::lexical_cast<int>(s);
	}
	catch (boost::bad_lexical_cast e)
	{
		throw Hash::MalformedCommand;
	}
}

void Writer::Write(Hash::Type t)
{
	Write(Hash::StringFromHash(t));
}

void Writer::Write(Database::Type t)
{
	Write((int) t);
}

void Writer::Write(double d)
{
	Write(boost::lexical_cast<string>(d));
}
