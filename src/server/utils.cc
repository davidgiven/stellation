#include "globals.h"
#include "utils.h"
#include <boost/random.hpp>
#include <sys/time.h>

static boost::rand48 prng(CurrentTime() * 1000.0);
static boost::uniform_01<boost::rand48> fprng(prng);

int Random(int range)
{
	return prng() % range;
}

double Random()
{
	return fprng();
}

double CurrentTime()
{
	struct timeval tv;
	gettimeofday(&tv, NULL);

	double t = tv.tv_sec;
	t += (double)tv.tv_usec / 1e6;
	return t;
}

