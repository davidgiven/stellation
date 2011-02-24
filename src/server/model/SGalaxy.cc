#include "globals.h"
#include "SGalaxy.h"
#include "SStar.h"

SGalaxy::SGalaxy(DatabaseObject& dbo):
	SObject(dbo),
	SGalaxyProperties(dbo)
{
}

void SGalaxy::OnAdditionOf(SObject& o)
{
	SStar star(o);
	if ((double)star.Brightness > 0.0)
		VisibleStars.AddToSet(star);
}

void SGalaxy::OnRemovalOf(SObject& o)
{
	SStar star(o);
	if ((double)star.Brightness > 0.0)
		VisibleStars.RemoveFromSet(star);
}

