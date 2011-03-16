package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.model.ObjectChangedHandler;
import com.cowlark.stellation3.common.model.SObject;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class Monitor<T extends SObject> implements ObjectChangedHandler
{
	private T _object;
	private HandlerRegistration _changeListener;
	
	public Monitor(T object)
    {
		_object = object;
    }
	
	public void attach()
	{
		_changeListener = _object.addObjectChangedHandler(this);
		update(_object);
	}
	
	public void detach()
	{
		_changeListener.removeHandler();
	}
	
	@Override
	public void onObjectChanged(SObject object)
	{
		update(_object);
	}
	
	protected abstract void update(T object);
	public abstract void emitControllers(List<Controller> list);
}
