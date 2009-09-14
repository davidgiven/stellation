/* AppEngine persistent storage plugin.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/GAEPersistenceInterface.java,v $
 * $Date: 2009/09/14 22:21:04 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.server.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletConfig;
import com.cowlark.stellation2.server.Log;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class GAEPersistenceInterface extends PersistenceInterface
{
	private static final String KIND = "STELLATION2";
	private static final String SEMAPHORE = "SEMAPHORE";
	private static final String DATA = "DATA";
	
	private DatastoreService _ds = DatastoreServiceFactory.getDatastoreService(); 

	public GAEPersistenceInterface(ServletConfig config)
	{
	}
	
	@Override
	public boolean isCached()
	{
	    return false;
	}
	
	@Override
	public void save(Map<Long, RootObject> db) throws IOException
	{
		Log.log("saving database");
		long now = System.currentTimeMillis();
		
		Entity entity = new Entity(KIND, DATA);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		GZIPOutputStream gzos = new GZIPOutputStream(os);
		ObjectOutputStream oos = new ObjectOutputStream(gzos);
		
		oos.writeObject(db);
		
		oos.close();
		gzos.close();
		os.flush();
		
		Blob blob = new Blob(os.toByteArray());
		entity.setProperty("data", blob);
		
		_ds.put(entity);
		
		long delta = System.currentTimeMillis() - now;
		Log.log("saved database in " + delta + "ms");
	}
	
	@Override
	public Map<Long, RootObject> load() throws IOException
	{
		try
		{
			Log.log("loading database");
			long now = System.currentTimeMillis();

			Key key = KeyFactory.createKey(KIND, DATA);
			Entity entity = _ds.get(key);
			
			Blob blob = (Blob) entity.getProperty("data");
			byte[] data = blob.getBytes();
			ByteArrayInputStream is = new ByteArrayInputStream(data);
			GZIPInputStream gzis = new GZIPInputStream(is);
			ObjectInputStream ois = new ObjectInputStream(gzis);
			
			Map<Long, RootObject> db = (Map<Long, RootObject>) ois.readObject();
			ois.close();
			
			long delta = System.currentTimeMillis() - now;
			Log.log("loaded database in " + delta + "ms");
			
			return db;
		}
		catch (ClassNotFoundException e)
		{
			throw new IOException(e.toString());
		}
		catch (EntityNotFoundException e)
		{
			return new HashMap<Long, RootObject>();
		}
	}
	
	private void sleep(long ms)
	{	
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
		}
	}
		
	private long atomicallyModify(int delta)
	{
		Key key = KeyFactory.createKey(KIND, SEMAPHORE);
		
		for (;;)
		{
			try
			{
				Transaction tx = _ds.beginTransaction();
				Entity entity;
				
				try
				{
					entity = _ds.get(tx, key);
				}
				catch (EntityNotFoundException e)
				{
					entity = new Entity(KIND, SEMAPHORE);
					entity.setProperty("count", new Long(0));
				}
				
				long count = (Long) entity.getProperty("count");
				count += delta;
				entity.setProperty("count", (Long) count);
				
				_ds.put(tx, entity);
				tx.commit();
				return count;
			}
			catch (DatastoreFailureException e)
			{
			}
			
			sleep(1000);
		}
	}
	
	public void lock() throws IOException
	{
		for (;;)
		{
			/* Attempt to increment the lock count. */
			
			if (atomicallyModify(1) == 1)
			{
				/* If 1, then we successfully have the lock. */
				Log.log("locked semaphore");
				break;
			}
			
			/* Otherwise, someone else has the lock. Decrement, sleep for
			 * a bit, and retry. */
			
			atomicallyModify(-1);
			
			sleep(1000);
		}
		
		try
		{
			ServerDB.Instance.load();
		}
		catch (IOException e)
		{
			Log.log("unlocking semaphore");
			atomicallyModify(-1);
			
			throw e;
		}
	}
	
	public void unlock() throws IOException
	{
		ServerDB.Instance.save();
		
		Log.log("unlocking semaphore");
		atomicallyModify(-1);
	}
}
