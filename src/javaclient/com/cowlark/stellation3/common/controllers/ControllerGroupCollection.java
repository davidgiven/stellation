package com.cowlark.stellation3.common.controllers;

import java.util.Iterator;
import java.util.Vector;

public class ControllerGroupCollection implements Iterable<ControllerGroup>
{
	private Vector<ControllerGroup> _groups;
	
	public ControllerGroupCollection()
    {
	    _groups = new Vector<ControllerGroup>();
    }
	
	public void addMonitorCollection(ControllerGroup mc)
	{
		_groups.add(mc);
	}
	
	public ControllerGroup getSingleton()
	{
		assert(_groups.size() == 1);
		return _groups.get(0);
	}
	
	@Override
	public Iterator<ControllerGroup> iterator()
	{
		return _groups.iterator();
	}
}
