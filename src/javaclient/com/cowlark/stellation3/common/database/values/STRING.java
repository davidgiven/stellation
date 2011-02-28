package com.cowlark.stellation3.common.database.values;

import com.cowlark.stellation3.common.database.Reader;

public class STRING extends DATUM
{
	private String _value;
	
	public String get()
	{
		return _value;
	}
	
	@Override
	public void set(Reader r)
	{
		_value = r.readString();
	}
	
	@Override
	public void set(DATUM d)
	{
		STRING s = (STRING) d;
		_value = s._value;
	}
}
