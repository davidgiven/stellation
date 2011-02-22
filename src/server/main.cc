#include "globals.h"
#include "Database.h"
#include "worldcreation.h"
#include <iostream>

static string programname;
static string dbname;
static bool createdb = false;

class Error
{
public:
	Error()
	{ std::cerr << programname << ": "; }

	~Error()
	{ std::cerr << std::endl; exit(1); }

	template <class T> Error* operator << (const T& t)
	{ std::cerr << t; return this; }
};

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
	else
		syntaxerror();
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

	if (createdb)
		CreateWorld();

	Database& db = Database::GetInstance();
	db.Save(std::cout);

	return 0;
}
