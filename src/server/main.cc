#include "globals.h"
#include "Database.h"
#include "DatabaseObject.h"
#include "Datum.h"
#include "SObject.h"

Database database;

int main(int argc, const char* argv[])
{
	DatabaseObject& o = database.Create();
	SObject ob(o);

	ob.X.SetType(Datum::NUMBER);
	ob.X = 42;

	ob.X = ob.X + 1;

	printf("%d\n", (int)ob.X);

	return 0;
}
