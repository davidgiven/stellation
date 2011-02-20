#include "globals.h"
#include "Database.h"
#include "SGalaxy.h"
#include "SStar.h"
#include "utils.h"
#include <math.h>
#include <utility>
#include <boost/range/size.hpp>

using std::pair;

const int NUMBER_OF_STARS = 400;
const double GALAXY_RADIUS = 20;

static string generate_name()
{
	static const char* syllables1[] =
	{
		"An", "Ca", "Jo", "Ka", "Kri", "Da", "Re", "De", "Ed", "Ma", "Ni",
		"Qua", "Qa", "Li", "La", "In", "On", "An", "Un", "Ci", "Cu", "Ce",
		"Co", "Xa", "Xef", "Xii", "Xo'o", "Xu", "Ram", "Noq", "Mome", "Pawa",
		"Limi", "Ney"
	};

	static const char* syllables2[] =
	{
		"the", "ru", "shu", "be", "po", "fol", "boo", "qwa", "xi", "lo", "fi"
	};

	static const char* syllables3[] =
	{
		"drew", "rine", "vid", "a", "na", "sten", "niel", "cca", "vin", "ven",
		"cor", "rion", "rath", "tong", "lar", "bol", "ting", "narg", "aq", "blan",
		"sim", "pil", "rib", "org", "lig", "zim", "frob", "cha", "poo", "tang"
	};

	string s;
	s += RandomOf(syllables1);
	if (Random(2) == 0)
		s += RandomOf(syllables2);
	s += RandomOf(syllables3);
	return s;
}

void CreateWorld()
{
	SGalaxy galaxy(DBCreate());
	galaxy.Initialise();

	/* Create the appropriate number of names. */

	typedef set<string> NamesSet;
	NamesSet names;
	while (names.size() < NUMBER_OF_STARS)
	{
		string name = generate_name();
		names.insert(name);
	}

	/* Now determine the locations the stars. */

	typedef map<int, pair<double, double> > LocationsMap;
	LocationsMap locations;
	while (locations.size() < NUMBER_OF_STARS)
	{
		double r = Random() * GALAXY_RADIUS;
		double t = Random() * 2.0 * M_PI;

		double x = round(r * sin(t) * 10.0) / 10.0;
		double y = round(r * cos(t) * 10.0) / 10.0;

		int hash = ((int)x + GALAXY_RADIUS) * (GALAXY_RADIUS*2) +
                ((int)y + GALAXY_RADIUS);

		if (locations.find(hash) == locations.end())
			locations[hash] = pair<double, double>(x, y);
	}

	/* Now create the stars themselves. */

	LocationsMap::const_iterator locationIterator = locations.begin();
	NamesSet::const_iterator nameIterator = names.begin();
	for (int i = 0; i < NUMBER_OF_STARS; i++)
	{
		SStar star(DBCreate());
		star.Initialise();

		const pair<double, double>& l = (locationIterator++)->second;

		star.Name = *nameIterator++;
		star.X = l.first;
		star.Y = l.second;
		star.Brightness = 1.0 + Random()*9.0;
		star.AsteroidsC = Random(10) + 10;
		star.AsteroidsM = Random(10) + 10;

		galaxy.Add(star);
	}
}
