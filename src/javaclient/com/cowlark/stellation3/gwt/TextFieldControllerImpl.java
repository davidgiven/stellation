package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class TextFieldControllerImpl extends ControllerImpl
	implements TextFieldController
{
	private final TextFieldHandler _tfh;
	private final TextBox _textBox;
	
	public TextFieldControllerImpl(TextFieldHandler tfh, String label, boolean password)
    {
		_tfh = tfh;
		setLeft(label);
	
		if (password)
			_textBox = new PasswordTextBox();
		else
			_textBox = new TextBox();
		setRight(_textBox);
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
