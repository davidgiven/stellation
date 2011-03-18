package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.model.ObjectChangedHandler;
import com.cowlark.stellation3.common.model.SObject;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class Monitor<T extends SObject>
		implements ObjectChangedHandler, HasMonitors
{
	private T _object;
	private HasMonitors _parent;
	private HandlerRegistration _changeListener;
	
	public Monitor(T object)
    {
		_object = object;
    }
	
	public T getMonitoredObject()
	{
		return _object;
	}
	
	@Override
	public void attach(HasMonitors parent)
	{
		_parent = parent;
		_changeListener = _object.addObjectChangedHandler(this);
		update(_object);
	}
	
	@Override
	public void detach()
	{
		if (_parent != null)
		{
			_changeListener.removeHandler();
			_parent = null;
		}
	}
	
	@Override
	public void onObjectChanged(SObject object)
	{
		update(_object);
	}
	
	@Override
	public void updateAllMonitors()
	{
		if (_parent != null)
			_parent.updateAllMonitors();
	}
	
	protected abstract void update(T object);
	
	@Override
	public abstract void emitControllers(List<Controller> list);
}
