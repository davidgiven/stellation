package com.cowlark.stellation3.common.database.values;

import com.cowlark.stellation3.common.database.Reader;

public class NUMBER extends DATUM
{
	private double _value;
	
	public double get()
	{
		return _value;
	}
	
	@Override
	public void set(Reader r)
	{
		_value = r.readDouble();
	}
	
	@Override
	public void set(DATUM d)
	{
		NUMBER n = (NUMBER) d;
		_value = n._value;
	}
}
