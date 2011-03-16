package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.LabelController;
import com.google.gwt.user.client.ui.Label;

public class LabelControllerImpl extends ControllerImpl
	implements LabelController
{
	private final Label _label;
	
	public LabelControllerImpl(String label)
    {
		setLeft(label);
		
		_label = new Label();
		setRight(_label);
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
