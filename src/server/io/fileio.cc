#include "globals.h"
#include "Database.h"
#include "Reader.h"
#include "Writer.h"
#include "fileio.h"
#include <iostream>
#include <fstream>

class FileReader : public Reader
{
public:
	FileReader(const string& filename):
		_stream(filename.c_str())
	{
	}

private:
	std::ifstream _stream;
};

class FileWriter : public Writer
{
public:
	FileWriter(const string& filename):
		_stream(filename.c_str())
	{
	}

	void Write(const string& s)
	{
		_stream << s;
		_stream << '\0';
	}

private:
	std::ofstream _stream;
};

void SaveDatabaseToFile(const string& filename)
{
	FileWriter writer(filename);
	Database::GetInstance().Save(writer);
}
