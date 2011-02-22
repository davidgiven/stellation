#include "globals.h"
#include "DatabaseObject.h"
#include "Datum.h"
#include "SObject.h"
#include "Property.h"

SObject::SObject(DatabaseObject& dbo):
#include "property-accessors-cc.h"
	_dbo(dbo)
{
}

void SObject::Initialise()
{
	InitialiseClass(*this);
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
