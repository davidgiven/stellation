package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.database.Reader;
import com.google.gwt.core.client.JsArrayString;

public class JsArrayReader extends Reader
{
	private JsArrayString _data;
	private int _position;
	
	public JsArrayReader(JsArrayString data, int position)
	{
		_data = data;
		_position = position;
	}
	
	public JsArrayReader(JsArrayString data)
	{
		this(data, 0);
	}
	
	public boolean isEOF()
	{
		return (_position == _data.length());
	}
	
	public String readString()
	{
		assert(!isEOF());
		return _data.get(_position++);
	}
}
