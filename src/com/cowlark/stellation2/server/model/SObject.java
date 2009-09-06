/* Server-side generic object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SObject.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import java.util.Iterator;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.ResourcesNotAvailableException;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.ListOfServerObjects;
import com.cowlark.stellation2.server.db.Property;
import com.cowlark.stellation2.server.db.RootObject;

@CClass(name = CObject.class)
public class SObject extends RootObject implements Iterable<SObject>
{
    private static final long serialVersionUID = 237820976895490834L;

	@Property
	private long _changedTime;

	/* Not scoped --- this is handled manually. */
    @Property
	private ListOfServerObjects<SObject> _contents =
		new ListOfServerObjects<SObject>();

    @Property(scope = S.GLOBAL)
	private DBRef<SObject> _parent = new DBRef<SObject>();;
	
    @Property(scope = S.GLOBAL)
	private DBRef<SPlayer> _owner = new DBRef<SPlayer>();

    private SObject init()
    {
    	_changedTime = System.currentTimeMillis();
    	return this;
    }
    
    public SObject initialise()
    {
    	super.initialise(SUniverse.getStaticUniverse().getNextId());
       	return init();
    }
	
    public SObject initialise(long id)
    {
    	super.initialise(id);
       	return init();
    }
	
    public Iterator<SObject> iterator()
    {
    	return _contents.iterator();
    }
    
    public <T extends SObject> DBRef<T> getRef()
    {
    	return new DBRef<T>(getId());
    }
    
	public long getChangedTime()
    {
	    return _changedTime;
    }
	
	
	public ListOfServerObjects<SObject> getContents()
    {
    	return _contents;
    }

	public void setContents(ListOfServerObjects<SObject> contents)
    {
    	_contents = contents;
    	dirty();
    }

	public void setParent(DBRef<SObject> parent)
    {
    	_parent = parent;
    	dirty();
    }

	public SPlayer getOwner()
    {
	    return _owner.get();
    }
	
	public SObject setOwner(SPlayer owner)
    {
	    _owner = new DBRef<SPlayer>(owner);
	    dirty();
	    return this;
    }
	
	public SUniverse getUniverse()
	{
		return SUniverse.getStaticUniverse();
	}
	
	public SGalaxy getGalaxy()
	{
		return getUniverse().getGalaxy();
	}
	
	public void dirty()
	{
	    _changedTime = System.currentTimeMillis();
	}
	
	public SObject getLocation()
    {
	    return _parent.get();
    }

	public SFleet toFleet()
	{
		return null;
	}

	public SStar toStar()
	{
		return null;
	}
	
	public SCargoship toCargoship()
	{
		return null;
	}
	
	public long timerExpiry(STimer timer)
	{
		return S.CANCELTIMER;
	}
	
	public void onAdditionOf(SObject object)
	{
	}
	
	public void onAdditionTo(SObject parent)
	{
	}
	
	public void add(SObject object)
	{
		object.removeFromParent();
		object.onAdditionTo(this);
		onAdditionOf(object);
		_contents.add(object);
		object.setParent(new DBRef<SObject>(this));
		dirty();
	}
	
	public void add(DBRef<SObject> object)
	{
		add(object.get());
	}
	
	public void onRemovalFrom(SObject parent)
	{
	}
	
	public void onRemovalOf(SObject object)
	{
	}
	
	public void remove(SObject object)
	{
		object.onRemovalFrom(this);
		onRemovalOf(object);
		_contents.remove(object.<SObject>getRef());
		dirty();
	}
	
	public void remove(DBRef<SObject> object)
	{
		remove(object.get());
	}
	
	public void removeFromParent()
	{
		if (!_parent.isNull())
			_parent.get().remove(new DBRef<SObject>(this));
	}
	    
	public void destroy()
	{
		SObject parent = getLocation();
		if (parent != null)
			parent.remove(this);
		delete();
	}
	
	public void consume(Resources r) throws ResourcesNotAvailableException
	{
	}
	
    public STug createTug()
    {
    	STug o = new STug()
    		.initialise();
    	o.setOwner(getOwner());
    	add(o);
    	return o;
    }

    public SCargoship createCargoship()
    {
    	SCargoship o = new SCargoship()
    		.initialise();
    	o.setOwner(getOwner());
    	add(o);
    	return o;
    }

    public SJumpship createJumpship()
    {
    	SJumpship o = new SJumpship()
    		.initialise();
    	o.setOwner(getOwner());
    	add(o);
    	return o;
    }
}
