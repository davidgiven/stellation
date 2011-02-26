#include "globals.h"
#include "Log.h"
#include "SPlayer.h"
#include "SFleet.h"
#include "SUnit.h"
#include "SStar.h"

SStar::SStar(Database::Type oid):
	SObject(oid),
	SStarProperties(oid)
{
}

void SStar::CalculateVisibleObjects(ObjectSet& visible, SPlayer* player)
{
	Log() << "considering star " << this;
	for (ObjectSet::const_iterator i = Contents.SetBegin(),
			e = Contents.SetEnd(); i != e; i++)
	{
		Database::Type oid = *i;
		Hash::Type oidclass = SObject::GetClass(oid);

		visible.insert(oid);
		switch (oidclass)
		{
			case Hash::SFleet:
			{
				SFleet* fleet = SFleet::Get(oid);
				//fleet->CalculateVisibleObjects(visible, player);
				break;
			}

			default:
			{
				SUnit* unit = SUnit::Get(oid);
				break;
			}
		}
	}
}
