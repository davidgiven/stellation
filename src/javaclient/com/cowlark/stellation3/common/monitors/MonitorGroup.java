package com.cowlark.stellation3.common.monitors;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.game.Game;

public class MonitorGroup implements Iterable<Monitor<?>>
{
	private final Vector<Monitor<?>> _monitors;
	private final String _name;
	private GroupTitleController _nameController;
	
	public MonitorGroup(String name)
    {
	    _monitors = new Vector<Monitor<?>>();
	    _name = name;
	    if (!_name.isEmpty())
		    _nameController = Game.Instance.createGroupTitleController(_name);
    }
	
	public void addMonitor(Monitor<?> c)
	{
		_monitors.add(c);
	}
	
	public String getName()
	{
		return _name;
	}
	
	@Override
	public Iterator<Monitor<?>> iterator()
	{
		return _monitors.iterator();
	}
	
	public Vector<Controller> getControllers()
	{
		Vector<Controller> controllers = new Vector<Controller>();
		emitControllers(controllers);
		return controllers;
	}
	
	public void emitControllers(List<Controller> controllers)
	{
		if (_nameController != null)
			controllers.add(_nameController);
			
		for (Monitor<?> m : _monitors)
			m.emitControllers(controllers);
	}
	
	public void attach()
	{
		for (Monitor<?> monitor : _monitors)
			monitor.attach();
	}
	
	public void detach()
	{
		for (Monitor<?> monitor : _monitors)
			monitor.detach();
	}
}
