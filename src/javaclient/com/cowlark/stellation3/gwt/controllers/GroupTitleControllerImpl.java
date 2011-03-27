package com.cowlark.stellation3.gwt.controllers;

import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.gwt.ui.MarkupLabelWidget;

public class GroupTitleControllerImpl extends ControllerImpl
	implements GroupTitleController
{
	private String _markup;
	private MarkupLabelWidget _label;
	
	public GroupTitleControllerImpl(String markup)
    {
		super(1);
		_markup = markup;
		_label = new MarkupLabelWidget();
		_label.setStylePrimaryName("groupTitle");
		
		if (markup != null)
			setStringValue(markup);
		
		setCell(0, _label);
    }
	
	@Override
	public String getStringValue()
	{
	    return _markup;
	}
	
	@Override
	public void setStringValue(String value)
	{
		_markup = value;
		_label.setMarkup(_markup);
	}
}
