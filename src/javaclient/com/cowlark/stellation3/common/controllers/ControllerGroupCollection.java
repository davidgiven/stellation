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
	
	@Override
	public Iterator<ControllerGroup> iterator()
	{
		return _groups.iterator();
	}
}
