/* An abstract ordered map of objects in the database.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/MapOfObjects.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.cowlark.stellation2.common.Identifiable;
import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class MapOfObjects<T extends Identifiable>
		implements Map<String, T>, IsSerializable, Serializable
{
    private static final long serialVersionUID = 8550736697590760259L;
    
	private HashMap<String, Long> _data = new HashMap<String, Long>();

	public MapOfObjects()
    {
    }
	
	public MapOfObjects(Map<String, Long> ids)
    {
		_data.putAll(ids);
    }
	
	public Map<String, Long> getData()
	{
		return _data;
	}
	
	public void clear()
    {
		_data.clear();
    }

	public boolean containsKey(Object key)
    {
	    return _data.containsKey(key); 
    }

	public boolean containsValue(Object value)
    {
		T i = (T) value;
	    return _data.containsValue(i.getId());
    }

	private class ClientEntry implements Map.Entry<String, T>
	{
		private Map.Entry<String, Long> _entry;
		
		public ClientEntry(Map.Entry<String, Long> entry)
        {
			_entry = entry;
        }

		public String getKey()
        {
            return _entry.getKey();
        }

		public T getValue()
        {
			return get(_entry.getValue());
        }

		public T setValue(T value)
        {
			T old = getValue();
			_entry.setValue(value.getId());
			return old;
        }
	};
	
	public Set<Map.Entry<String, T>> entrySet()
    {
		HashSet<Map.Entry<String, T>> set = 
			new HashSet<Map.Entry<String, T>>();
		for (Map.Entry<String, Long> entry : _data.entrySet())
			set.add(new ClientEntry(entry));

		return set;
    }

	public boolean isEmpty()
    {
	    return _data.isEmpty();
    }

	public Set<String> keySet()
    {
	    return _data.keySet();
    }

	public T put(String key, T value)
    {
		T old = get(key);
		_data.put(key, value.getId());
	    return old;
    }

	public void putAll(Map<? extends String, ? extends T> t)
    {
		for (Map.Entry<? extends String, ? extends T> entry : t.entrySet())
			_data.put(entry.getKey(), entry.getValue().getId());
    }

	public T remove(Object key)
    {
	    Long id = _data.remove(key);
	    if (id != null)
	    	return get((long) id);
	    return null;
    }

	public int size()
    {
	    return _data.size();
    }

	public T get(Object key)
    {
		Long id = _data.get(key);
		if (id != null)
			return Database.get(id);
	    return null;
    }

	abstract public Collection<T> values();
}
