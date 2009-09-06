/* Abstract DataGroup that watches a particular database object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/ObjectWatcherDataGroup.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;

public abstract class ObjectWatcherDataGroup<T extends Identifiable> extends DataGroup
		implements ChangeCallback, Identifiable
{
    private static final long serialVersionUID = 5968930876004600598L;
    
	private DBRef<T> _object;
	
	public ObjectWatcherDataGroup(DBRef<T> object)
    {
	    _object = object; 
    }
	
	public ObjectWatcherDataGroup(T object)
    {
	    _object = new DBRef<T>(object.getId());; 
    }
	
	public long getId()
	{
	    return _object.getId();
	}
	
	public DBRef<T> getRef()
	{
		return _object;
	}
	
	public T getObject() throws OutOfScopeException
	{
		T o = _object.get();
		if (o == null)
			throw new OutOfScopeException(_object.getId());
		return o;
	}
	
	@Override
	protected void onLoad(DataTable table)
	{
	    super.onLoad(table);
	    ClientDB.addChangeCallback(_object.getId(), this);
	    onChange(this);
	}
	
	@Override
	protected void onUnload()
	{
	    ClientDB.removeChangeCallback(_object.getId(), this);
	    super.onUnload();
	}
	
	public abstract void onChange(ChangeCallback cb);
}
