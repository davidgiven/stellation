package com.cowlark.stellation3.common.model;

import com.google.gwt.event.shared.GwtEvent;

public class ObjectChangedEvent extends GwtEvent<ObjectChangedHandler>
{
	public static Type<ObjectChangedHandler> TYPE =
		new Type<ObjectChangedHandler>();
	
	private SObject _object;
	
	public ObjectChangedEvent(SObject object)
	{
		_object = object;
	}
	
	@Override
	public Type<ObjectChangedHandler> getAssociatedType()
	{
	    return TYPE;
	}
	
	@Override
	protected void dispatch(ObjectChangedHandler handler)
	{
		handler.onObjectChanged(_object);
	}
	
	public SObject getObject()
	{
		return _object;
	}
}
