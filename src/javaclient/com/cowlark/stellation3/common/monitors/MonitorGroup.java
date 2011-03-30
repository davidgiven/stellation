package com.cowlark.stellation3.common.monitors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.game.Game;

public class MonitorGroup implements Iterable<Monitor<?>>, HasMonitors
{
	private class Key
	{
		int oid;
		Class<?> type;
		
		public Key(Monitor<?> m)
		{
			oid = m.getMonitoredObject().Oid;
			type = m.getClass();
		}
		
		@Override
		public int hashCode()
		{
		    return type.hashCode() | (oid << 16);
		}
	};
	
	private final Vector<Monitor<?>> _monitors;
	private final HashMap<Key, Monitor<?>> _oldMonitors;
	private String _title;
	private HasMonitors _parent;
	private GroupTitleController _nameController;
	
	public MonitorGroup(String title)
    {
	    _monitors = new Vector<Monitor<?>>();
	    _oldMonitors = new HashMap<Key, Monitor<?>>();
	    setTitle(title);
    }
	
	public MonitorGroup()
	{
		this(null);
	}
	
	public void beginUpdate()
	{
		for (Monitor<?> m : _monitors)
		{
			Key k = new Key(m);
			_oldMonitors.put(k, m);
		}
		
		_monitors.clear();
	}
	
	public void addMonitor(Monitor<?> m)
	{
		Key k = new Key(m);
		Monitor<?> existing = _oldMonitors.get(k);
		if (existing != null)
		{
			m = existing;
			_oldMonitors.remove(k);
		}
		else if (_parent != null)
			m.attach(_parent);
			
		_monitors.add(m);
	}
	
	public void endUpdate()
	{
		if (_parent != null)
		{
			for (Monitor<?> m : _oldMonitors.values())
				m.detach();
		}
			
		_oldMonitors.clear();
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public void setTitle(String name)
	{
	    _title = name;
	    if (_title != null)
	    {
	    	if (_nameController == null)
			    _nameController = Game.Instance.createGroupTitleController(_title);
	    	else
	    		_nameController.setStringValue(_title);
	    }
	    else
	    	_nameController = null;
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
	
	@Override
	public void emitControllers(List<Controller> controllers)
	{
		if (_nameController != null)
			controllers.add(_nameController);
			
		for (Monitor<?> m : _monitors)
			m.emitControllers(controllers);
	}
	
	@Override
	public void attach(HasMonitors parent)
	{
		_parent = parent;
		for (Monitor<?> monitor : _monitors)
			monitor.attach(parent);
	}
	
	@Override
	public void detach()
	{
		for (Monitor<?> monitor : _monitors)
			monitor.detach();
		_parent = null;
	}
	
	@Override
	public void updateAllMonitors()
	{
		if (_parent != null)
			_parent.updateAllMonitors();
	}
}
