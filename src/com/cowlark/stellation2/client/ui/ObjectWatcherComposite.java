/* Abstract widget that watches a particular database object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/ObjectWatcherComposite.java,v $
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
import com.google.gwt.user.client.ui.Composite;

public abstract class ObjectWatcherComposite<T extends Identifiable> extends Composite
		implements ChangeCallback, Identifiable
{
	private DBRef<T> _object;
	
	public ObjectWatcherComposite(DBRef<T> object)
    {
		super();
	    _object = object; 
    }
	
	public ObjectWatcherComposite(T object)
    {
		super();
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
	protected void onLoad()
	{
	    super.onLoad();
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
