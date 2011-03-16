package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.gwt.ControllerImpl;

public class GroupTitleControllerImpl extends ControllerImpl
	implements GroupTitleController
{
	public GroupTitleControllerImpl(String title)
    {
		setLeft(title);
    }
	
	@Override
	public String getTitle()
	{
	    return getLeft();
	}
}
