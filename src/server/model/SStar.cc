#include "globals.h"
#include "SStar.h"

SStar::SStar(DatabaseObject& dbo):
	SObject(dbo)
{
}

void SStar::Initialise()
{
	SObject::Initialise();

	X.SetType(Datum::NUMBER);
	Y.SetType(Datum::NUMBER);
	Brightness.SetType(Datum::NUMBER);
	Name.SetType(Datum::STRING);
	AsteroidsM.SetType(Datum::NUMBER);
	AsteroidsC.SetType(Datum::NUMBER);
}
