package com.cowlark.stellation3.common.database.values;

import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.database.Reader;

public class TOKEN extends DATUM
{
	private Hash _value;
	
	public Hash get()
	{
		return _value;
	}
	
	@Override
	public void set(Reader r)
	{
		_value = r.readHash();
	}
	
	@Override
	public void set(DATUM d)
	{
		TOKEN t = (TOKEN) d;
		_value = t._value;
	}
}
