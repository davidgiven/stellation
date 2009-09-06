/* Client-side generic object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CObject.java,v $
 * $Date: 2009/09/06 22:15:12 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.common.model;

import java.util.Iterator;
import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.client.view.AbstractView;
import com.cowlark.stellation2.client.view.FallbackView;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.db.ListOfClientObjects;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;
import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class CObject implements IsSerializable,
		Iterable<CObject>, Identifiable
{
	@Property
	public long _id;
	
	@Property
	public int _scope;
	
	@Property
	public DBRef<CObject> _parent = new DBRef<CObject>();
	
	@Property
	public ListOfClientObjects _contents = new ListOfClientObjects();
	
	@Property
	public DBRef<CPlayer> _owner = new DBRef<CPlayer>();

	public CObject()
    {
    }
	
	@Override
	public int hashCode()
	{
		return (int) _id;
	}
	
	/* --- Converters ---------------------------------------------------- */
	
	public CPlayer toPlayer()
	{
		return null;
	}
	
	/* --- Getters/setters ----------------------------------------------- */
	
	public Properties getProperties()
	{
		return PropertyStore.Generic;
	}
	
	public String getDescription()
	{
		return "This object has no description yet.";
	}
	
	public long getId()
    {
	    return _id;
    }

	public void setId(long id)
    {
	    _id = id;
    }
	
	public int getScope()
    {
	    return _scope;
    }
	
	public void setScope(int scope)
    {
	    _scope = scope;
    }
	
	public CObject getParent()
    {
    	return _parent.get();
    }

	public ListOfClientObjects getContents()
    {
    	return _contents;
    }

	public void setContents(ListOfClientObjects contents)
    {
	    _contents = contents;
    }
	
	public CPlayer getOwner()
    {
	    return _owner.get();
    }
	
	public Iterator<CObject> iterator()
	{
	    return _contents.iterator();
	}

	/* --- Converters ---------------------------------------------------- */

	public CStar toStar()
	{
		return null;
	}
	
	public CFleet toFleet()
	{
		return null;
	}
	
	/* --- Scope & visibility -------------------------------------------- */
	
	public void checkNear() throws OutOfScopeException
	{
		switch (_scope)
		{
			case S.OWNER:
			case S.LOCAL:
				return;
				
			case S.GLOBAL:
				throw new OutOfScopeException(getId());
		}
	}
	
	public void checkOwner() throws OutOfScopeException
	{
		switch (_scope)
		{
			case S.OWNER:
				return;
				
			case S.LOCAL:
			case S.GLOBAL:
				throw new OutOfScopeException(getId());
		}
	}
	
	public CUniverse getUniverse()
	{
		return Stellation2.getUniverse();
	}
	
	public CGalaxy getGalaxy()
	{
		return getUniverse().getGalaxy();
	}
	
	public void addChangeCallback(ChangeCallback cb)
	{
		assert(_id != S.NULL);
		ClientDB.addChangeCallback(_id, cb);
	}

	public void removeChangeCallback(ChangeCallback cb)
	{
		assert(_id != S.NULL);
		ClientDB.removeChangeCallback(_id, cb);
	}
	
	public AbstractView<?> createRightPaneView()
	{
		return new FallbackView(this);
	}
}
