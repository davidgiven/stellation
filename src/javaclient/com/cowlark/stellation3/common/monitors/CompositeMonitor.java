package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.model.SObject;

public abstract class CompositeMonitor<T extends SObject> extends Monitor<T>
{
	private MonitorGroup _group;
	
	public CompositeMonitor(T object)
    {
		super(object);
		_group = new MonitorGroup();
    }
	
	protected void beginUpdate()
	{
		_group.beginUpdate();
	}
	
	protected void addMonitor(Monitor<?> m)
	{
		_group.addMonitor(m);
	}
	
	protected void endUpdate()
	{
		_group.endUpdate();
	}
	
	protected abstract void update(T object);
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		_group.emitControllers(list);
	}
	
	@Override
	public void attach(HasMonitors parent)
	{
	    super.attach(parent);
	    _group.attach(parent);
	}
	
	@Override
	public void detach()
	{
		_group.detach();
	    super.detach();
	}
}
