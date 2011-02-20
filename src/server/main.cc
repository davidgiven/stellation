#include "globals.h"
#include "Database.h"
#include "worldcreation.h"

int main(int argc, const char* argv[])
{
	CreateWorld();

	Database& db = Database::GetInstance();
	db.Save(std::cout);

	return 0;
}
