#ifndef OKVSTORE_H
#define OKVSTORE_H

template <class OBJECTTYPE, class KEYTYPE, class VALUETYPE>
class okvstore
{
public:
	typedef std::pair<OBJECTTYPE, KEYTYPE> pair_type;

private:
	typedef std::map<KEYTYPE, VALUETYPE> Level2Map;
	typedef std::map<OBJECTTYPE, Level2Map> Level1Map;

public:
	okvstore()
	{ }

	/* Fetch a pointer to an item in the datastore, or NULL if no such
	 * item exists.
	 */

	VALUETYPE* getptr(const OBJECTTYPE& oid, const KEYTYPE& kid)
	{
		typename Level1Map::iterator i1 = _data.find(oid);
		if (i1 == _data.end())
			return NULL;

		Level2Map& l2 = i1->second;
		typename Level2Map::iterator i2 = l2.find(kid);
		if (i2 == l2.end())
			return NULL;
		return &(i2->second);
	}

	/* Return a reference to an item in the datastore; one will be made if
	 * necessary.
	 */

	VALUETYPE& get(const OBJECTTYPE& oid, const KEYTYPE& kid)
	{
		Level2Map& l2 = _data[oid];

		if (l2.find(kid) == l2.end())
			dirty(oid, kid);

		return l2[kid];
	}

	/* Visit all values in the store. */

	template <class VISITOR> void visit(VISITOR& visitor)
	{
		for (typename Level1Map::const_iterator i = _data.begin(),
				e = _data.end(); i != e; i++)
		{
			const OBJECTTYPE& oid = i->first;
			const Level2Map& l2 = i->second;

			for (typename Level2Map::const_iterator j = l2.begin(),
					e = l2.end(); j != e; j++)
			{
				const KEYTYPE& kid = j->first;
				const VALUETYPE& value = j->second;

				visitor(oid, kid, value);
			}
		}
	}

	void dirty(const OBJECTTYPE& oid, const KEYTYPE& kid)
	{
		const pair_type p(oid, kid);

		/* Check to see if this item is already in the log. */

		if (_changedEntries.find(p) != _changedEntries.end())
		{
			/* Yes --- nothing to do (don't want to erase any previously
			 * saved entries!
			 */

			return;
		}

		/* Mark the value as being changed. */

		_changedEntries.insert(p);

		/* Retrieve the old value, if any, and insert it into the log. */

		VALUETYPE* value = getptr(oid, kid);
		if (value)
			_savedData.push_back(LogEntry(oid, kid, *value));
	}

	void commit()
	{
		/* Discard the log, as we don't need to do any rollback. */

		_changedEntries.clear();
		_savedData.clear();
	}

	void rollback()
	{
		/* Restore every saved data item. */

		while (!_savedData.empty())
		{

			const LogEntry& le = _savedData.front();
			_data[le.oid][le.kid] = le.value;

			_changedEntries.erase(pair_type(le.oid, le.kid));

			_savedData.pop_front();
		}

		/* Anything else that's been changed must be a new item, so remove
		 * it.
		 */

		while (!_changedEntries.empty())
		{
			typename ChangedEntries::iterator i = _changedEntries.begin();

			const pair_type& p = *i;
			_data[p.first].erase(p.second);

			_changedEntries.erase(i);
		}

		/* Check everything is empty. */

		assert(_savedData.empty());
		assert(_changedEntries.empty());
	}

	void diagnostics(int& changed)
	{
		changed = _changedEntries.size();
	}

private:
	struct LogEntry : public noncopyable
	{
		LogEntry(OBJECTTYPE oid, KEYTYPE kid, VALUETYPE value):
			oid(oid), kid(kid), value(value)
		{}

		LogEntry(const LogEntry& other):
			oid(other.oid), kid(other.kid), value(other.value)
		{}

		OBJECTTYPE oid;
		KEYTYPE kid;
		VALUETYPE value;
	};

	Level1Map _data;

	typedef std::set<pair_type> ChangedEntries;
	ChangedEntries _changedEntries;

	typedef std::list<LogEntry> SavedData;
	SavedData _savedData;
};

#endif
