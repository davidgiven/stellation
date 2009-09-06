/* A collection of one object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/SingletonCollection.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingletonCollection<T> implements Iterable<T>
{
	private T _item;
	
	public SingletonCollection(T item)
    {
	    _item = item;
    }

	public Iterator<T> iterator()
	{
		class MyIterator implements Iterator<T>
		{
			private boolean _hasNext = true;
			
			public boolean hasNext()
			{
			    return _hasNext;
			}
			
			public T next()
			{
				if (_hasNext)
				{
					_hasNext = false;
					return _item;
				}
				else
					throw new NoSuchElementException();
			}
			
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		}
		
		return new MyIterator();
	}
}
