package com.cowlark.stellation3.common.monitors;

import java.util.Iterator;
import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;

public class MonitorGroupCollection implements Iterable<MonitorGroup>,
		PaneHandler
{
	private Vector<MonitorGroup> _groups;
	private Pane _attached;
	
	public MonitorGroupCollection()
    {
	    _groups = new Vector<MonitorGroup>();
    }
	
	public void addMonitorGroup(MonitorGroup mc)
	{
		_groups.add(mc);
	}
	
	public MonitorGroup getSingleton()
	{
		assert(_groups.size() == 1);
		return _groups.get(0);
	}
	
	public MonitorGroup createMonitorGroup(String label)
	{
		MonitorGroup mg = new MonitorGroup(label);
		addMonitorGroup(mg);
		return mg;
	}
	
	@Override
	public Iterator<MonitorGroup> iterator()
	{
		return _groups.iterator();
	}
	
	public Vector<Controller> getControllers()
	{
		Vector<Controller> controllers = new Vector<Controller>();
		
		for (MonitorGroup group : _groups)
		{
			group.emitControllers(controllers);
		}
		
		return controllers;
	}
	
	public void attach(Pane pane)
	{
		_attached = pane;
		
		for (MonitorGroup group : _groups)
			group.attach();
		
		pane.updateControllers(getControllers());
	}
	
	public void detach()
	{
		for (MonitorGroup group : _groups)
			group.detach();
		
		_attached = null;
	}
	
	@Override
	public void onPaneCancelled(Pane d)
	{
		detach();
	}
	
	@Override
	public void onPaneClosed(Pane d)
	{
		detach();
	}
}
