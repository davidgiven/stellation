#include "globals.h"
#include "Database.h"
#include "Log.h"
#include "worldcreation.h"
#include "Transport.h"
#include "utils.h"
#include "mainloop.h"
#include "fileio.h"
#include <iostream>

static string programname;
static string dbname;
static set<string> zmqspec;
static bool createdb = false;

Error::Error()
{
	std::cerr << programname << ": ";
}

Error::~Error()
{
	std::cerr << std::endl;
	exit(1);
}

Error& Error::operator << (const string& s)
{
	std::cerr << s;
	return *this;
}

static double boottime = CurrentTime();
Log::Log()
{
	char buffer[16];
	sprintf(buffer, "%.03f", CurrentTime() - boottime);

	std::cout << buffer << ": ";
}

Log::~Log()
{
	std::cout << std::endl;
}

Log& Log::operator << (Database::Type i)
{
	std::cout << "#" << (int)i;

	return *this;
}

Log& Log::operator << (Hash::Type i)
{
	std::cout << "@" << i << "=" << Hash::StringFromHash(i);

	return *this;
}

Log& Log::operator << (int i)
{
	std::cout << i;
	return *this;
}

Log& Log::operator << (const string& s)
{
	std::cout << s;
	return *this;
}

static void syntaxerror()
{
	Error() << "syntax error (try --help for help)\n";
}

static void help()
{
	extern unsigned int help_length;
	extern const char help_data[];

	std::cerr << programname << ": help:\n"
			<< string(help_data, help_length);
	exit(1);
}

static int handle_parameter(const string& param, const string& next)
{
	if ((param == "-h") || (param == "--help"))
	{
		help();
	}
	else if ((param == "-c") || (param == "--create"))
	{
		createdb = true;
		return 0;
	}
	else if ((param == "-l") || (param == "--listen"))
	{
		zmqspec.insert(next);
		return 1;
	}
	else
		syntaxerror();
	return 0;
}

static void handle_file(const string& filename)
{
	if (!dbname.empty())
		Error() << "you can only specify one database file";
	dbname = filename;
}

static void parse_parameters(const char* const * argv)
{
	for (;;)
	{
		const char* thisparam = *argv++;
		const char* nextparam = thisparam ? *argv : NULL;

		if (!thisparam)
			break;
		if (!nextparam)
			nextparam = "";

		if (thisparam[0] == '-')
		{
			switch (thisparam[1])
			{
				case 0: /* single hypen as parameter --- syntax error */
					syntaxerror();

				case '-': /* double hyphen long parameter */
				{
					const char* equals = strchr(thisparam, '=');
					if (equals)
					{
						string left(thisparam, equals - thisparam);
						(void) handle_parameter(left, equals + 1);
					}
					else
						argv += handle_parameter(thisparam, nextparam);
					break;
				}

				default: /* single hyphen short parameter */
				{
					string left(thisparam, 2);
					if (thisparam[2])
						(void) handle_parameter(left, thisparam + 2);
					else
						argv += handle_parameter(left, nextparam);
				}
			}
		}
		else
			handle_file(thisparam);
	}
}

int main(int argc, const char* argv[])
{
	programname = argv[0];
	parse_parameters(argv + 1);

	if (dbname.empty())
	{
		Error() << "you must specify a database file";
	}

	if (createdb)
	{
		Log() << "creating database";
		CreateWorld();
		DatabaseCommit();
		Log() << "done database creation, saving new database";
		SaveDatabaseToFile(dbname);
		Log() << "finished save";
	}

	if (zmqspec.empty())
		Error() << "you must specify at least one listener specification";

	Mainloop(zmqspec);
	return 0;
}
