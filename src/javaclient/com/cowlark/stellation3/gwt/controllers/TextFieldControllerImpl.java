package com.cowlark.stellation3.gwt.controllers;

import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.cowlark.stellation3.gwt.ControllerImpl;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class TextFieldControllerImpl extends ControllerImpl
	implements TextFieldController
{
	private final TextFieldHandler _tfh;
	private final TextBox _textBox;
	
	public TextFieldControllerImpl(TextFieldHandler tfh, String label, boolean password)
    {
		super(2);
		_tfh = tfh;
		setCell(0, label);
	
		if (password)
			_textBox = new PasswordTextBox();
		else
			_textBox = new TextBox();
		setCell(1, _textBox);
    }
	
	@Override
	public String getStringValue()
	{
	    return _textBox.getText();
	}
	
	@Override
	public void setStringValue(String value)
	{
		_textBox.setText(value);
	}
}
