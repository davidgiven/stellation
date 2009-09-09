/* Client-side database management.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ClientDB.java,v $
 * $Date: 2009/09/09 23:18:12 $
 * $Author: dtrg $
 * $Revision: 1.5 $
 */

package com.cowlark.stellation2.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.UpdateBatch;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.common.db.LogMessageStore;
import com.cowlark.stellation2.common.model.CObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class ClientDB extends Database
{
	private static AsyncCallback<UpdateBatch> _cb = new ResponseHandler();
	private static Map<Long, CObject> _cache;
	private static LogMessageStore _logs = new LogMessageStore();
	private static long _lastUpdate;
	private static Map<Long, Set<ChangeCallback>> _callbacks;
	private static Set<ChangeCallback> _logCallbacks;
	private static Set<ChangeCallback> _anyChangeCallbacks;
		
	static
	{
		clear();
	}
	
	public <T extends Identifiable> T getImpl(long id)
	{
		return (T) _cache.get(id);
	}
	
	public <T extends Identifiable> void putImpl(T object)
	{
		long id = object.getId();
		_cache.put(id, (CObject) object);
	}
	
	public static void clear()
	{
		_lastUpdate = 0;
		_cache = new HashMap<Long, CObject>();
		_callbacks = new HashMap<Long, Set<ChangeCallback>>();
		_logCallbacks = new HashSet<ChangeCallback>();
		_anyChangeCallbacks = new HashSet<ChangeCallback>();
	}
	
	private static Set<ChangeCallback> getCallbackSet(long id)
	{
		Set<ChangeCallback> set = _callbacks.get(id);
		if (set == null)
		{
			set = new HashSet<ChangeCallback>();
			_callbacks.put(id, set);
		}
		
		return set;
	}
	
	public static void addChangeCallback(long id, ChangeCallback cb)
	{
		getCallbackSet(id).add(cb);
	}
	
	public static void addChangeCallback(CObject o, ChangeCallback cb)
	{
		addChangeCallback(o.getId(), cb);
	}
	
	public static void removeChangeCallback(long id, ChangeCallback cb)
	{
		getCallbackSet(id).remove(cb);
	}
	
	public static void removeChangeCallback(CObject o, ChangeCallback cb)
	{
		removeChangeCallback(o.getId(), cb);
	}
	
	public static void addLogChangeCallback(ChangeCallback cb)
	{
		_logCallbacks.add(cb);
	}
	
	public static void removeLogChangeCallback(ChangeCallback cb)
	{
		_logCallbacks.remove(cb);
	}
	
	public static void addChangeCallback(ChangeCallback cb)
	{
		_anyChangeCallbacks.add(cb);
	}
	
	public static void removeChangeCallback(ChangeCallback cb)
	{
		_anyChangeCallbacks.remove(cb);
	}
	
	public static AsyncCallback<UpdateBatch> getResponseHandler()
	{
		return _cb;
	}
	
	public static void fetchUpdates()
	{
		Stellation2.Service.ping(Stellation2.getAuthentication(), _lastUpdate,
				getResponseHandler());
	}

	private static void fireCallbacks(Set<ChangeCallback> cbs)
	{
		if (cbs == null)
			return;
		
		/* Copy the callback set to prevent issues if the callback
		 * tries to register more callbacks. */
		
		Vector<ChangeCallback> cbcopy = new Vector<ChangeCallback>(cbs);
		for (ChangeCallback cb : cbcopy)
			cb.onChange(cb);
	}
	
	public static void processBatch(UpdateBatch result)
	{
		_lastUpdate = result.getTime();
		Stellation2.setTimeDelta(_lastUpdate - System.currentTimeMillis());

		/* Merge in any new log messages. */
		
		_logs.merge(result.getLogUpdates());
		fireCallbacks(_logCallbacks);
		
		/* Add new objects. */
		
		for (CObject object : result)
			_cache.put(object.getId(), object);
		
		/* Delete non-visible objects. */
		
		Set<Long> invisible = new HashSet<Long>(_cache.keySet());
		invisible.removeAll(result.getVisibleObjects());
		for (Long id : invisible)
			_cache.remove(id);
		
		/* Fire the callback on newly invisible items (so the viewers get to
		 * see that they've gone). */
		
		for (long id : invisible)
		{
			Set<ChangeCallback> set = _callbacks.get(id);
			fireCallbacks(set);
		}
		
		/* Fire the callback on other modified objects. */
		
		for (CObject object : result)
		{
			Set<ChangeCallback> set = _callbacks.get(object.getId());
			fireCallbacks(set);
		}
		
		/* Fire the global change callbacks. */
		
		fireCallbacks(_anyChangeCallbacks);
	}
	
	public static String dumpVisibleDatabase()
	{
		try
		{
			SerializationStreamFactory streamFactory =
				(SerializationStreamFactory) Stellation2.Service;
				
			SerializationStreamWriter ssw = streamFactory.createStreamWriter();
			ssw.writeObject(_cache.get(-1));
			return ssw.toString();
		}
		catch (SerializationException e)
		{
			return null;
		}
	}
	
	public static LogMessageStore getLogs()
    {
	    return _logs;
    }
	
	/* RPC calls */

	public static void cargoshipLoadUnload(
			long id, double antimatter, double metal, double organics)
	{
		Stellation2.Service.cargoshipLoadUnload(
				Stellation2.getAuthentication(), _lastUpdate,
				id, antimatter, metal, organics,
				getResponseHandler());		
	}	
}
