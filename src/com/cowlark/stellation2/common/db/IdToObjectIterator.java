/* Adaptor iterator converting from object ID to object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/IdToObjectIterator.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.util.Iterator;
import com.cowlark.stellation2.common.Identifiable;

public final class IdToObjectIterator<T extends Identifiable> implements Iterator<T>
{
	private final Iterator<Long> _iterator;
	private T _next;
	
	public IdToObjectIterator(Iterator<Long> iterator)
    {
		_iterator = iterator;
    }
	
	public final boolean hasNext()
	{
		if (_next != null)
			return true;
		
		while (_iterator.hasNext())
		{
			long id = _iterator.next();
			_next = Database.get(id);
			if (_next != null)
				return true;
		}
		
		return false;
	}
	
	public final T next()
	{
		T o = _next;
		_next = null;
		return o;
	}
	
	public final void remove()
	{
		_iterator.remove();
	}
}