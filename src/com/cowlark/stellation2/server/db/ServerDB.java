/* Server-side database management.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/ServerDB.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.server.Log;

public class ServerDB extends Database
{
	public static ServerDB Instance;
	
	public static class RootObjectToKeyIterator implements Iterator<Long>
	{
		private Iterator<RootObject> _iterator;
		
		public RootObjectToKeyIterator(Iterator<RootObject> iterator)
        {
			_iterator = iterator;
        }
		
		public boolean hasNext()
		{
		    return _iterator.hasNext();
		}
		
		public Long next()
		{
			RootObject o = _iterator.next();
		    return o.getId();
		}
		
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	public static class RootObjectToKeyIterable implements Iterable<Long>
	{
		Iterable<RootObject> _iterable;
		
		public RootObjectToKeyIterable(Iterable<RootObject> iterable)
        {
			_iterable = iterable;
        }
		
		public Iterator<Long> iterator()
		{
		    return new RootObjectToKeyIterator(_iterable.iterator());
		}
	}
	
	private Map<Long, RootObject> _db = new HashMap<Long, RootObject>();
	private final PersistenceInterface _persistence;

	public ServerDB(PersistenceInterface persistence)
    {
		_persistence = persistence;
		
		if (_persistence.isCached())
		{
		    try
		    {
		    	load();
		    }
		    catch (IOException e)
		    {
		    	Log.log("unable to load database");
		    }			
		}
    }
	
	public void close()
	{
		if (_persistence.isCached())
		{
		    try
		    {
		    	save();
		    }
		    catch (IOException e)
		    {
		    	Log.log("unable to save database");
		    }			
		}
	}
	
	public void load() throws IOException
	{
		_db = _persistence.load();
	}
	
	public void save() throws IOException
	{
		_persistence.save(_db);
	}
	
	public void lock() throws IOException
	{
		_persistence.lock();
	}
	
	public void unlock() throws IOException
	{
		_persistence.unlock();
	}
	
	final void announceNewObject(RootObject object)
	{
		_db.put(object.getId(), object);
	}
	
	void setDeleted(RootObject object)
	{
		_db.remove(object.getId());
	}

	public <T extends Identifiable> T getImpl(long id)
	{
		return (T) _db.get(id);
	}
	
	public <T extends Identifiable> void putImpl(T object)
	{
		long id = object.getId();
		_db.put(id, (RootObject) object);
	}
}
