package com.cowlark.stellation3.gwt.controllers;

import com.cowlark.stellation3.common.controllers.Controller;

public class ControllerImpl implements Controller
{
	private Object[] _data;
	private String _style;
	
	public ControllerImpl(int cells)
    {
		_data = new Object[cells];
    }
	
	public void setCell(int cell, Object o)
	{
		assert(cell < _data.length);
		assert(cell >= 0);
		assert(_data[cell] == null);
		_data[cell] = o;
	}
	
	public int getCells()
	{
		return _data.length;
	}
	
	public Object getCell(int cell)
	{
		if (cell >= _data.length)
			return null;
		return _data[cell];
	}
	
	public void setStyle(String style)
	{
		_style = style;
	}
	
	public String getStyle()
	{
		return _style;
	}
}
