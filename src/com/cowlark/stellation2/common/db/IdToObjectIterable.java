/* Adaptor iterable converting from object ID to object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/IdToObjectIterable.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.util.Iterator;
import com.cowlark.stellation2.common.Identifiable;

public final class IdToObjectIterable<T extends Identifiable> implements Iterable<T>
{
	final private Iterable<Long> _iterable;
	
	public IdToObjectIterable(Iterable<Long> iterable)
    {
		_iterable = iterable;
    }
	
	public final Iterator<T> iterator()
	{
	    return new IdToObjectIterator<T>(_iterable.iterator());
	}
}
