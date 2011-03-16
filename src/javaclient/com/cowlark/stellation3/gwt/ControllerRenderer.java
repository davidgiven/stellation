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
		_container.clear(true);
		int y = 0;
		
		int maxcols = 0;
		for (Controller c : controllers)
		{
			ControllerImpl ci = (ControllerImpl) c;
			maxcols = Math.max(maxcols, ci.getCells());
		}
		
		for (Controller c : controllers)
		{
			ControllerImpl ci = (ControllerImpl) c;
		
			int width = ci.getCells();
			for (int i = 0; i < width; i++)
			{
				Object o = ci.getCell(i);
				if (o instanceof String)
					_container.setText(y, i, (String)o);
				else if (o instanceof Widget)
					_container.setWidget(y, i, (Widget)o);
				else if (o != null)
					assert(false);
				
				_container.getFlexCellFormatter().
					setColSpan(y, i, 1);
			}
			
			if (width < maxcols)
			{
				_container.getFlexCellFormatter().
					setColSpan(y, width-1, 1 + maxcols - width);
			}
			
			y++;
		}
	}
}
