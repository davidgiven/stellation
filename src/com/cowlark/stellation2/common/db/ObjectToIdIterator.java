/* Adaptor iterator converting from object to object ID.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/ObjectToIdIterator.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.util.Iterator;
import com.cowlark.stellation2.common.Identifiable;

public final class ObjectToIdIterator<T extends Identifiable> implements Iterator<Long>
{
	private final Iterator<T> _iterator;
	
	public ObjectToIdIterator(Iterator<T> iterator)
    {
		_iterator = iterator;
    }
	
	public final boolean hasNext()
	{
	    return _iterator.hasNext();
	}
	
	public final Long next()
	{
	    return _iterator.next().getId();
	}
	
	public final void remove()
	{
		_iterator.remove();
	}
}
