package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.gwt.ControllerImpl;

public class GroupTitleControllerImpl extends ControllerImpl
	implements GroupTitleController
{
	public GroupTitleControllerImpl(String title)
    {
		super(1);
		setCell(0, title);
    }
	
	@Override
	public String getTitle()
	{
	    return (String) getCell(0);
	}
}
