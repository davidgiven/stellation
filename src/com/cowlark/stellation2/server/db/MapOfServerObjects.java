/* An ordered map of client-side database objects.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/MapOfServerObjects.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import com.cowlark.stellation2.common.db.HasDBRepresentation;
import com.cowlark.stellation2.common.db.MapOfClientObjects;
import com.cowlark.stellation2.common.db.MapOfObjects;

public class MapOfServerObjects<T extends RootObject> extends MapOfObjects<T>
		implements HasDBRepresentation, Serializable
{
    private static final long serialVersionUID = -941207130613390041L;

	public MapOfServerObjects()
    {
		super();
    }
	
	public MapOfServerObjects(Map<String, Long> ids)
    {
		super(ids);
    }
	
	public Iterable<String> toDBRepresentation()
	{
		LinkedList<String> list = new LinkedList<String>();
    	for (Map.Entry<String, Long> entry : getData().entrySet())
    	{
    		list.add(entry.getKey());
			list.add(Long.toString(entry.getValue()));
    	}
		return list;
	}

	public void fromDBRepresentation(Iterable<String> db)
	{
		Iterator<String> i = db.iterator();
		Map<String, Long> data = getData();
		while (i.hasNext())
		{
			String key = i.next();
			String value = i.next();
			long id = Long.parseLong(value);
			data.put(key, id);
		}
	}

	public Object getClient()
    {
		return new MapOfClientObjects(getData());
    }
	
	public Collection<T> values()
    {
		ListOfServerObjects<T> values = new ListOfServerObjects<T>();
		
		for (Long id : getData().values())
			values.add(id);
		
	    return values;
    }
}
