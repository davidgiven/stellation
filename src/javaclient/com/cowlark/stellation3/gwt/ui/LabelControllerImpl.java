package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.controllers.LabelController;
import com.cowlark.stellation3.gwt.ControllerImpl;
import com.google.gwt.user.client.ui.Label;

public class LabelControllerImpl extends ControllerImpl
	implements LabelController
{
	private final Label _label;
	
	public LabelControllerImpl(String label)
    {
		super(2);
		setCell(0, label);
		
		_label = new Label();
		setCell(1, _label);
    }
	
	@Override
	public String getStringValue()
	{
	    return _label.getText();
	}
	
	@Override
	public void setStringValue(String value)
	{
		_label.setText(value);
	}
}
