package com.cowlark.stellation3.gwt;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class ControllerRenderer
{
	private FlexTable _container;
	
	public ControllerRenderer()
    {
		_container = new FlexTable();
    }
	
	public FlexTable getContainer()
	{
		return _container;
	}
	
	public void update(List<Controller> controllers)
	{
		_container.clear();
		int y = 0;
		
		for (Controller c : controllers)
		{
			ControllerImpl ci = (ControllerImpl) c;
		
			String left = ci.getLeft();
			if (left != null)
				_container.setText(y, 0, left);
			
			Widget right = ci.getRight();
			if (right != null)
				_container.setWidget(y, 1, right);
			
			y++;
		}
	}
}
