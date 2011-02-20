#include "globals.h"
#include "DatabaseObject.h"
#include "Datum.h"
#include "SObject.h"

#include "accessors-cc.h"

SObject::SObject(DatabaseObject& dbo):
	DATUM_INITIALISERS /* no comma here */
	_dbo(dbo)
{
}
