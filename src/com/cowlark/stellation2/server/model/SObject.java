/* Server-side generic object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SObject.java,v $
 * $Date: 2009/09/20 21:45:48 $
 * $Author: dtrg $
 * $Revision: 1.8 $
 */

package com.cowlark.stellation2.server.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.InvalidObjectException;
import com.cowlark.stellation2.common.exceptions.ResourcesNotAvailableException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.ListOfServerObjects;
import com.cowlark.stellation2.server.db.Property;
import com.cowlark.stellation2.server.db.RootObject;

@CClass(name = CObject.class)
public class SObject extends RootObject implements Iterable<SObject>
{
    private static final long serialVersionUID = 237820976895490834L;

	private static Map<Integer, Class<? extends SObject>> _classes =
		new HashMap<Integer, Class<? extends SObject>>();
	static
	{
		_classes.put(PropertyStore.CARGOSHIP, SCargoship.class);
		_classes.put(PropertyStore.JUMPSHIP, SJumpship.class);
		_classes.put(PropertyStore.TUG, STug.class);
		_classes.put(PropertyStore.BASICFACTORY, SBasicFactory.class);
	}	

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
    
	public Properties getProperties()
	{
		return PropertyStore.Generic;
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
	
	public void dirtyAll()
	{
		dirty();
		for (SObject o : this)
			o.dirtyAll();
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

	public SUnit toUnit()
	{
		return null;
	}
	
	public SCargoship toCargoship()
	{
		return null;
	}
	
	public STug toTug()
	{
		return null;
	}
	
	public SFactory toFactory()
	{
		return null;
	}
	
	public long timerExpiry(STimer timer)
	{
		return S.CANCELTIMER;
	}
	
	public SStar getStar()
	{
		SObject o = this;
		while (!(o instanceof SStar))
		{
			o = o.getLocation();
			if (o == null)
				return null;
		}
		return (SStar) o;
	}

	public void onAdditionOf(SObject object)
	{
		_contents.add(object);
	}
	
	public void onAdditionTo(SObject parent)
	{
	}
	
	public void add(SObject object)
	{
		object.removeFromParent();
		object.onAdditionTo(this);
		onAdditionOf(object);
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
		_contents.remove(object.<SObject>getRef());
	}
	
	public void remove(SObject object)
	{
		object.onRemovalFrom(this);
		onRemovalOf(object);
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
	
	public <T extends SObject> T createObject(SPlayer owner, int type)
	{
		try
		{
			T o = (T) _classes.get(type).newInstance();
			o.initialise();
			o.setOwner(owner);
			add(o);
			return o;
		}
		catch (IllegalAccessException e)
		{
			assert false;
		}
		catch (InstantiationException e)
		{
			assert false;
		}
		return null;
	}
	
    public STug createTug(SPlayer owner)
    {
    	return createObject(owner, PropertyStore.TUG);
    }

    public SCargoship createCargoship(SPlayer owner)
    {
    	return createObject(owner, PropertyStore.CARGOSHIP);
    }

    public SJumpship createJumpship(SPlayer owner)
    {
    	return createObject(owner, PropertyStore.JUMPSHIP);
    }
    
    public SBasicFactory createBasicFactory(SPlayer owner)
    {
    	return createObject(owner, PropertyStore.BASICFACTORY);
    }
    
	public void checkObjectVisibleTo(SPlayer player)
		throws StellationException
	{
		throw new InvalidObjectException(getId());
	}
	
	public void checkObjectOwnedBy(SPlayer player)
		throws StellationException
	{
		if (getOwner() != player)
			throw new InvalidObjectException(getId());
	}
}
