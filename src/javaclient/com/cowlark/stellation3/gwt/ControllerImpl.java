package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.Controller;
import com.google.gwt.user.client.ui.Widget;

public class ControllerImpl implements Controller
{
	private String _left;
	private Widget _right;
	
	public ControllerImpl()
    {
    }
	
	public void setLeft(String left)
	{
		assert(_left == null);
		_left = left;
	}
	
	public String getLeft()
	{
		return _left;
	}
	
	public void setRight(Widget right)
	{
		assert(_right == null);
		_right = right;
	}
	
	public Widget getRight()
	{
		return _right;
	}
}
