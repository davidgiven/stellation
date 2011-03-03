#include "globals.h"
#include "SUniverse.h"
#include "SPlayer.h"
#include "Reader.h"
#include "Log.h"
#include "utils.h"
#include "auth.h"
#include <ctype.h>
#include <sstream>
#include <boost/multi_index_container.hpp>
#include <boost/multi_index/member.hpp>
#include <boost/multi_index/hashed_index.hpp>

const static double MAXIMUM_COOKIE_AGE = 24 * 3600; // one day

struct CookieRecord : public noncopyable
{
	Database::Type player;
	string cookie;
	double creationTime;

	CookieRecord():
		player(Database::Null),
		creationTime(0.0)
	{ }

	CookieRecord(const CookieRecord& other)
	{
		*this = other;
	}

	CookieRecord& operator = (const CookieRecord& other)
	{
		player = other.player;
		cookie = other.cookie;
		creationTime = other.creationTime;
	}
};

struct oid_index {};
struct cookie_index {};

typedef boost::multi_index_container
<
	CookieRecord,
	boost::multi_index::indexed_by
	<
		boost::multi_index::hashed_unique
		<
			boost::multi_index::tag<oid_index>,
			boost::multi_index::member
			<
				CookieRecord,
				Database::Type,
				&CookieRecord::player
			>
		>,
		boost::multi_index::hashed_unique
		<
			boost::multi_index::tag<cookie_index>,
			boost::multi_index::member
			<
				CookieRecord,
				string,
				&CookieRecord::cookie
			>
		>
	>
>
CookieStore;

typedef CookieStore::index<oid_index>::type OidIndex;
typedef CookieStore::index<cookie_index>::type CookieIndex;

static CookieStore cookieStore;

static string create_cookie()
{
	std::stringstream s;

	for (int i = 0; i < 32; i++)
	{
		char c = 'a' + Random(26);
		if (Random(2) == 0)
			c = toupper(c);
		s << c;
	}

	return s.str();
}

string CreateAuthenticationCookie(Reader& reader)
{
	string email = reader.ReadString();
	string password = reader.ReadString();
	if (!reader.IsEOF())
		throw Hash::MalformedCommand;

	SUniverse universe(Database::Universe);
	Database::Type playeroid = universe.Players.FetchFromMap(email);

	Log() << "authenticating " << email << " (who is " << playeroid << ")";
	if (playeroid == Database::Null)
	{
		Log() << "attempt to authenticate unknown player " << email;
		throw Hash::AuthenticationFailure;
	}

	SPlayer player(playeroid);
	if (player.Password != password)
	{
		Log() << "incorrect login from " << email;
		throw Hash::AuthenticationFailure;
	}

	/* Find or create this entry in the store. */

	OidIndex& index = cookieStore.get<oid_index>();
	OidIndex::iterator recordi = index.find(playeroid);

	CookieRecord record;
	if (recordi == index.end())
	{
		/* New entry. */

		record.player = playeroid;
		record.cookie = create_cookie();
		record.creationTime = CurrentTime();

		recordi = index.insert(index.end(), record);
	}
	else
	{
		/* Existing entry --- update the cookie if it's too old. */

		record = *recordi;
		if (record.creationTime < (CurrentTime() - MAXIMUM_COOKIE_AGE))
		{
			record.cookie = create_cookie();
			record.creationTime = CurrentTime();
			index.replace(recordi, record);
		}
	}

	Log() << "authentication cookie is: " << record.cookie;
	return record.cookie;
}

Database::Type CheckAuthenticationCookie(const string& cookie)
{
	CookieIndex& index = cookieStore.get<cookie_index>();
	CookieIndex::const_iterator recordi = index.find(cookie);

	if (recordi == index.end())
		return Database::Null;

	const CookieRecord& record = *recordi;
	return record.player;
}
