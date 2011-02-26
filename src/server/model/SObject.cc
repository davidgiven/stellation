#include "globals.h"
#include "Datum.h"
#include "SObject.h"
#include "Property.h"
#include "statics.h"

SObject::SObject(Database::Type oid):
	SObjectProperties(oid),
	_oid(oid)
{
}

void SObject::Initialise(Database::Type owner)
{
	InitialiseClass(*this);
	Owner = owner;
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

double SObject::GetNumberStatic(Hash::Type kid)
{
	return ::GetNumberStatic(GetClass(), kid);
}

string SObject::GetStringStatic(Hash::Type kid)
{
	return ::GetStringStatic(GetClass(), kid);
}
