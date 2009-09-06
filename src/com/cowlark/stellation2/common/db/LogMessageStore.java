/* Log message storage.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/LogMessageStore.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import com.google.gwt.user.client.rpc.IsSerializable;

public class LogMessageStore implements HasDBRepresentation,
		Iterable<Map.Entry<Long, String>>, Serializable, IsSerializable,
		Comparator<Long>
{
    private static final long serialVersionUID = -6123212076978072367L;
    
	private SortedMap<Long, String> _data = new TreeMap<Long, String>(this);
	
	public LogMessageStore()
    {
    }
	
	private LogMessageStore(SortedMap<Long, String> data)
	{
		_data.putAll(data);
	}
	
	public int compare(Long o1, Long o2)
	{
		return o2.compareTo(o1);
	}
	
	public Iterable<String> toDBRepresentation()
	{
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<Long, String> entry : _data.entrySet())
		{
			list.add(entry.getKey().toString());
			list.add(entry.getValue());
		}
		return list;
	}
	
	public void fromDBRepresentation(Iterable<String> value)
	{
		_data.clear();
		
		Iterator<String> i = value.iterator();
		while (i.hasNext())
		{
			String t = i.next();
			String v = i.next();
			
			_data.put(Long.parseLong(t), v);
		}
	}
	
	public Object getClient()
	{
	    return this;
	}
	
	public void merge(LogMessageStore store)
	{
		_data.putAll(store._data);
	}
	
	public LogMessageStore getSubset(long t1, long t2)
	{
		SortedMap<Long, String> subdata = _data.subMap(t1, t2);
		return new LogMessageStore(subdata);
	}
	
	public LogMessageStore add(long time, String message)
	{
		_data.put(time, message);
		return this;
	}
	
	public Iterator<Entry<Long, String>> iterator()
	{
	    return _data.entrySet().iterator();
	}
	
	public int size()
	{
		return _data.size();
	}
}
