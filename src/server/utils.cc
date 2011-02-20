#include "globals.h"
#include "utils.h"
#include <boost/random.hpp>

static boost::rand48 prng;
static boost::uniform_01<boost::rand48> fprng(prng);

int Random(int range)
{
	return prng() % range;
}

double Random()
{
	return fprng();
}
