/* An abstract ordered set of objects in the database.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/ListOfObjects.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.Utils;
import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class ListOfObjects<T extends Identifiable> implements
		Collection<T>, IsSerializable, Serializable
{
    private static final long serialVersionUID = 3720299452119305728L;
    
	private TreeSet<Long> _data = new TreeSet<Long>();

	public ListOfObjects()
    {
    }
	
	public ListOfObjects(Collection<Long> ids)
    {
		_data.addAll(ids);
    }
	
	public Set<Long> getData()
	{
		return _data;
	}
	
	public boolean add(long id)
	{
		return _data.add(id);
	}
	
	public boolean add(T o)
    {
	    return _data.add(o.getId());
    }

	public boolean addAllIds(Collection<Long> collection)
    {
		return _data.addAll(collection);
    }

	public boolean addAll(Collection<? extends T> collection)
    {
		boolean changed = false;
		for (T o : collection)
		{
			if (_data.add(o.getId()))
				changed = true;
		}
	    return changed;
    }

	public void clear()
    {
		_data.clear();
    }

	public boolean contains(Object o)
    {
		T to = (T) o;
	    return _data.contains(to.getId());
    }

	public boolean containsAll(Collection<?> collection)
    {
		if (collection.isEmpty())
			return false;
		
		for (Object o : collection)
		{
			if (o instanceof Identifiable)
			{
				Identifiable io = (Identifiable) o;
				if (!_data.contains(io.getId()))
					return false;
			}
			else
				return false;
		}
	    return true;
    }

	public boolean isEmpty()
    {
	    return _data.isEmpty();
    }

	public Iterator<T> iterator()
    {
	    return new IdToObjectIterator<T>(_data.iterator());
    }

	public Set<Long> getIds()
	{
		return _data;
	}
	
	public boolean remove(Object o)
    {
		T to = (T) o;
	    return _data.remove(to.getId());
    }

	public boolean removeAll(Collection<?> collection)
    {
		boolean changed = false;
		for (Object o : collection)
		{
			if (remove(o))
				changed = true;
		}
	    return changed;
    }

	public boolean retainAll(Collection<?> collection)
    {
		HashSet<Long> current = new HashSet<Long>(_data);
		for (Object o : collection)
		{
			Identifiable io = (Identifiable) o;
			current.remove(io.getId());
		}
		
	    return _data.removeAll(current);
    }

	public int size()
    {
	    return _data.size();
    }

	public Object[] toArray()
    {
		Object[] array = new Object[_data.size()];
	    return toArray(array);
    }

	public <T> T[] toArray(T[] array)
    {
		int i = 0;
		for (Object o : this)
		{
			T so = (T) o;
			array[i] = so;
			i++;
		}
		
	    return array;
    }

	public T getRandomElement()
	{
		long n = Utils.random(_data.size());
		for (long id : _data)
		{
			if (n == 0)
				return Database.get(id);
			n--;
		}
		
		return null;
	}
}
