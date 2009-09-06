/* A reference to an object in the database.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/DBRef.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.io.Serializable;
import java.util.Iterator;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.SingletonCollection;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DBRef<T extends Identifiable> implements IsSerializable, Identifiable,
		Comparable<DBRef<T>>, HasDBRepresentation, Serializable
{
    private static final long serialVersionUID = -4038009853755279099L;

	public static <T extends Identifiable> DBRef<T> NULL()
	{
		return new DBRef<T>();
	}
	
	private long _id;

	public DBRef()
    {
	    _id = S.NULL;
    }
	
	public DBRef(long id)
    {
		_id = id;
    }
	
	public DBRef(T id)
	{
		_id = id.getId();
	}
	
	@Override
	public int hashCode()
	{
	    return (int) _id;
	}
	
	public boolean equals(DBRef<T> other)
	{
		return _id == other._id;
	}
	
	public long getId()
	{
		return _id;
	}
	
	public boolean isNull()
	{
		return (_id == S.NULL);
	}
	
	public int compareTo(DBRef<T> o)
	{
		Long id = _id;
	    return id.compareTo(o.getId());
	}
	
	public T get()
	{
		return Database.get(_id);
	}
	
	public void fromDBRepresentation(Iterable<String> value)
	{
		Iterator<String> i = value.iterator();
		String s = i.next();
		
		_id = Long.parseLong(s);
	}
	
	public Iterable<String> toDBRepresentation()
	{
		String s = Long.toString(_id);
		return new SingletonCollection<String>(s);
	}
	
	public Object getClient()
	{
		return this;
	}
}
