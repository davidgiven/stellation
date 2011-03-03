package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class ControllerRenderer
{
	public static FlexTable createRendererContainer()
	{
		return new FlexTable();
	}
	
	public static void renderGroup(FlexTable ft, ControllerGroup cg)
	{
		int y = ft.getRowCount();
		for (Controller abstractController : cg)
		{
			ControllerImpl ci = (ControllerImpl) abstractController;
			
			String left = ci.getLeft();
			if (left != null)
				ft.setText(y, 0, left);
			
			Widget right = ci.getRight();
			if (right != null)
				ft.setWidget(y, 1, right);
			
			y++;
		}
	}
}
