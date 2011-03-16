package com.cowlark.stellation3.common.model;

import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.database.SBase;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class SObject extends SBase
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
			case SJumpship:  return new SJumpship(oid);
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
}
