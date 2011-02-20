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

void SObject::Initialise()
{
	Type.SetType(Datum::STRING);
	Type = GetType();

	InitProperty(Contents,        Datum::OBJECTSET,     Datum::LOCAL);
}

void SObject::InitProperty(Datum& datum, Datum::Type type, Datum::Scope scope)
{
	datum.SetType(type);
}

void SObject::Add(SObject& o)
{
	Datum& contents = Contents;
	assert(!contents.InSet(o));

	contents.AddToSet(o);
	OnAdditionOf(o);
}

void SObject::Remove(SObject& o)
{
	Datum& contents = Contents;
	assert(contents.InSet(o));

	OnRemovalOf(o);
	contents.RemoveFromSet(o);
}

void SObject::OnAdditionOf(SObject& o)
{
}

void SObject::OnRemovalOf(SObject& o)
{
}
