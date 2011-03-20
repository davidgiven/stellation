#include "globals.h"
#include "Database.h"
#include "SUniverse.h"
#include "SGalaxy.h"
#include "SStar.h"
#include "SPlayer.h"
#include "SFleet.h"
#include "Log.h"
#include "statics.h"
#include "utils.h"
#include <math.h>
#include <utility>
#include <boost/range/size.hpp>

using std::pair;

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
	int number_of_stars = GetNumberStatic(Hash::SGalaxy, Hash::Count);
	double galactic_radius = GetNumberStatic(Hash::SGalaxy, Hash::Radius);

	SUniverse* universe = SUniverse::Create(Database::Null);
	SGalaxy* galaxy = SGalaxy::Create(Database::Null);
	*universe->Galaxy = galaxy;

	/* Create the appropriate number of names. */

	typedef set<string> NamesSet;
	NamesSet names;
	while (names.size() < number_of_stars)
	{
		string name = generate_name();
		names.insert(name);
	}

	/* Now determine the locations the stars. */

	typedef map<int, pair<double, double> > LocationsMap;
	LocationsMap locations;
	while (locations.size() < number_of_stars)
	{
		double r = Random() * galactic_radius;
		double t = Random() * 2.0 * M_PI;

		double x = round(r * sin(t) * 10.0) / 10.0;
		double y = round(r * cos(t) * 10.0) / 10.0;

		int hash = ((int)x + galactic_radius) * (galactic_radius*2) +
                ((int)y + galactic_radius);

		if (locations.find(hash) == locations.end())
			locations[hash] = pair<double, double>(x, y);
	}

	/* Now create the stars themselves. */

	LocationsMap::const_iterator locationIterator = locations.begin();
	NamesSet::const_iterator nameIterator = names.begin();
	for (int i = 0; i < number_of_stars; i++)
	{
		SStar* star = SStar::Create(Database::Null);

		const pair<double, double>& l = (locationIterator++)->second;

		*star->Name = *nameIterator++;
		*star->X = l.first;
		*star->Y = l.second;
		*star->Brightness = 1.0 + Random()*9.0;
		*star->AsteroidsC = Random(10) + 10;
		*star->AsteroidsM = Random(10) + 10;

		galaxy->AllLocations->AddToSet(star);
		galaxy->VisibleStars->AddToSet(star);
	}
}

void CreatePlayer(const string& name, const string& empirename,
		const string& email, const string& password)
{
	Database::Type playeroid = FindPlayer(email);
	if (playeroid != Database::Null)
		throw Hash::PlayerAlreadyExists;

	SUniverse* universe = SUniverse::Get(Database::Universe);
	SGalaxy* galaxy = SGalaxy::Get(universe->Galaxy);

	SPlayer* player = SPlayer::Create(Database::Null);
	*player->Owner = player;

	*player->Name = name;
	*player->EmpireName = empirename;
	*player->Email = email;
	*player->Password = password;
	RegisterPlayer(email, *player);

	SStar* star = SStar::Get(galaxy->VisibleStars->RandomSetMember());

	SFleet* fleet = player->CreateFleet(star, name + "'s starter fleet");
	fleet->CreateJumpship();

	Log() << "Created player " << player << ": " << name
			<< " of " << empirename << ", aka " << email;
}
