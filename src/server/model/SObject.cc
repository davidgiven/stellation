#include "globals.h"
#include "Datum.h"
#include "SObject.h"
#include "SShip.h"
#include "SPlayer.h"
#include "SStar.h"
#include "SGalaxy.h"
#include "SUniverse.h"
#include "SJumpship.h"
#include "SFleet.h"
#include "Property.h"
#include "statics.h"

static map<Database::Type, shared_ptr<SObject> > proxyCache;

static SObject* create_proxy(Database::Type oid, Hash::Type classtoken)
{
	switch (classtoken)
	{
#include "proxy-table.h"
		default:
			throw Hash::ObjectDoesNotExist;
	}
}

Hash::Type SObject::GetClass(Database::Type oid)
{
	return *(TokenDatum*) DatabaseGet(oid, Hash::Class);
}

SObject* SObject::Get(Database::Type oid)
{
	shared_ptr<SObject>& o = proxyCache[oid];
	if (o)
		return o.get();

	o.reset(create_proxy(oid, GetClass(oid)));
	return o.get();
}

SObject* SObject::Create(Hash::Type classtoken, Database::Type owner)
{
	Database::Type oid = DatabaseAllocateOid();
	shared_ptr<SObject>& o = proxyCache[oid];
	o.reset(create_proxy(oid, classtoken));

	o->Initialise(owner);

	return o.get();
}

void SObject::FlushCache()
{
	proxyCache.clear();
}

SObject::SObject(Database::Type oid):
	SObjectProperties(oid),
	_oid(oid)
{
}

SObject::~SObject()
{
}

void SObject::Initialise(Database::Type owner)
{
	InitialiseClass(*this);
	*Owner = owner;
}

void SObject::Add(SObject* o)
{
	assert(!Contents->InSet(o));

	Contents->AddToSet(o);
	*o->Location = this;
	OnAdditionOf(o);
}

void SObject::Remove(SObject* o)
{
	assert(Contents->InSet(o));

	OnRemovalOf(o);
	Contents->RemoveFromSet(o);
	*o->Location = Database::Null;
}

void SObject::OnAdditionOf(SObject* o)
{
}

void SObject::OnRemovalOf(SObject* o)
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
