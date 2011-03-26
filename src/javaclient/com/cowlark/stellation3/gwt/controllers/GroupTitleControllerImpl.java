package com.cowlark.stellation3.gwt.controllers;

import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.google.gwt.user.client.ui.Label;

public class GroupTitleControllerImpl extends ControllerImpl
	implements GroupTitleController
{
	private String _title;
	private Label _label;
	
	public GroupTitleControllerImpl(String title)
    {
		super(1);
		_title = title;
		_label = new Label();
		_label.setStylePrimaryName("groupTitle");
		
		if (title != null)
			setStringValue(title);
		
		setCell(0, _label);
    }
	
	@Override
	public String getStringValue()
	{
	    return _title;
	}
	
	@Override
	public void setStringValue(String value)
	{
		_title = value;
		_label.setText(value);
	}
}
