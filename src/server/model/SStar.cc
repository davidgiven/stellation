#include "globals.h"
#include "SStar.h"

SStar::SStar(DatabaseObject& dbo):
	SObject(dbo)
{
}

void SStar::Initialise()
{
	SObject::Initialise();

	InitProperty(X,               Datum::NUMBER,        Datum::GLOBAL);
	InitProperty(Y,               Datum::NUMBER,        Datum::GLOBAL);
	InitProperty(Brightness,      Datum::NUMBER,        Datum::GLOBAL);
	InitProperty(Name,            Datum::STRING,        Datum::GLOBAL);
	InitProperty(AsteroidsM,      Datum::NUMBER,        Datum::LOCAL);
	InitProperty(AsteroidsC,      Datum::NUMBER,        Datum::LOCAL);
}
