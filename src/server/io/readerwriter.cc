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
		return 0;
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

void Writer::Write(int i)
{
	Write(boost::lexical_cast<string>(i));
}

void Writer::Write(double d)
{
	Write(boost::lexical_cast<string>(d));
}
