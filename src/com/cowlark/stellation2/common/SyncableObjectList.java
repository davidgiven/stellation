/* A syncable list of objects.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/SyncableObjectList.java,v $
 * $Date: 2009/09/20 21:50:35 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class SyncableObjectList<T extends Identifiable>
{
    private static final long serialVersionUID = 6918879521480736314L;

    private TreeMap<Long, T> _map = new TreeMap<Long, T>();
    
    public void syncWith(Collection<Identifiable> ids, SyncableObjectManager<T> callback)
    {
		Set<Long> newids = new HashSet<Long>();
		Set<Long> deletedids = new HashSet<Long>();
		deletedids.addAll(_map.keySet());
		
		for (Identifiable i : ids)
		{
			long id = i.getId();
			deletedids.remove(id);
			if (!_map.containsKey(id))
				newids.add(id);
		}
		
		for (long id : deletedids)
		{
			T t = _map.remove(id);
			if (t != null)
				callback.destroy(t);
		}
		
		for (long id : newids)
		{
			T t = callback.create(id);
			_map.put(id, t);
		}
    }
    
    public boolean isEmpty()
    {
    	return _map.isEmpty();
    }
}
