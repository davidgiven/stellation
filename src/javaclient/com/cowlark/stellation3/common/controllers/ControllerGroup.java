package com.cowlark.stellation3.common.controllers;

import java.util.Iterator;
import java.util.Vector;

public class ControllerGroup implements Iterable<Controller>
{
	private final Vector<Controller> _controllers;
	private final String _name;
	
	public ControllerGroup(String name)
    {
	    _controllers = new Vector<Controller>();
	    _name = name;
    }
	
	public void addController(Controller c)
	{
		_controllers.add(c);
	}
	
	public String getName()
	{
		return _name;
	}
	
	@Override
	public Iterator<Controller> iterator()
	{
		return _controllers.iterator();
	}
}
