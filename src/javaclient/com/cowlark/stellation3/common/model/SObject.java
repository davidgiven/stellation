package com.cowlark.stellation3.common.model;

import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.database.SBase;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.monitors.MonitorGroup;
import com.cowlark.stellation3.common.monitors.ObjectIdMonitor;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class SObject extends SBase implements Comparable<SObject>
{
	private HandlerManager _handlerManager;
	
	public static SObject create(int oid, Hash type)
	{
		switch (type)
		{
			case SObject:    return new SObject(oid);
			case SUniverse:  return new SUniverse(oid);
			case SGalaxy:    return new SGalaxy(oid);
			case SStar:      return new SStar(oid);
			case SPlayer:    return new SPlayer(oid);
			case SFleet:     return new SFleet(oid);
			case SUnit:      return new SUnit(oid);
			case SShip:      return new SShip(oid);
			case SJumpship:  return new SJumpship(oid);
			case STug:       return new STug(oid);
			case SCargoship: return new SCargoship(oid);
			default:         assert(false);
		}
		
		return null;
	}
	
	public int Oid;
	
	public SObject(int oid)
	{
		Oid = oid;
		_handlerManager = new HandlerManager(this);
	}
	
	public HandlerRegistration addObjectChangedHandler(ObjectChangedHandler handler)
	{
		return _handlerManager.addHandler(ObjectChangedEvent.TYPE, handler);
	}
	
	public void objectChanged()
	{
		ObjectChangedEvent event = new ObjectChangedEvent(this);
		_handlerManager.fireEvent(event);
	}
	
	@Override
	public int compareTo(SObject o)
	{
		return new Integer(Oid).compareTo(o.Oid);
	}
	
	public void constructBasicControlPanelGroup(MonitorGroup group)
	{
		group.setTitle(getName());
		group.addMonitor(new ObjectIdMonitor(this));
	}
		
	public void createControlPanel(MonitorGroup group)
	{
		constructBasicControlPanelGroup(group);
	}
	
	public String getName()
	{
		if (Name != null)
			return Name.get();
		else
			return Game.Instance.Static.getString(Class.get(), Hash.Name);
	}
}
