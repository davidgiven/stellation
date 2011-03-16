package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.GroupTitleController;

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
