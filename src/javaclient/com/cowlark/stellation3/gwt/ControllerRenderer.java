package com.cowlark.stellation3.gwt;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.gwt.controllers.ControllerImpl;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class ControllerRenderer implements IsWidget
{
	private FlexTable _container;
	
	public ControllerRenderer()
    {
		_container = new FlexTable();
		_container.setWidth("100%");
		_container.setHeight("100%");
    }
	
	public FlexTable getContainer()
	{
		return _container;
	}
	
	@Override
	public Widget asWidget()
	{
	    return getContainer();
	}
	
	public void update(List<Controller> controllers)
	{
		_container.removeAllRows();
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
				{
					String s = (String)o;
					_container.setText(y, i, s);
				}
				else if (o instanceof Widget)
					_container.setWidget(y, i, (Widget)o);
				else if (o != null)
					assert(false);
				
				_container.getFlexCellFormatter().
					setColSpan(y, i, 1);
				
				String style = ci.getStyle();
				if (style != null)
					_container.getRowFormatter().addStyleName(y, style);
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
