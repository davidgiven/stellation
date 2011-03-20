package com.cowlark.stellation3.common.database.values;

import com.cowlark.stellation3.common.database.Reader;

public class StringDatum extends Datum
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
	public void set(Datum d)
	{
		StringDatum s = (StringDatum) d;
		_value = s._value;
	}
}
