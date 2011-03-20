package com.cowlark.stellation3.common.database.values;

import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.database.Reader;

public class TokenDatum extends Datum
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
	public void set(Datum d)
	{
		TokenDatum t = (TokenDatum) d;
		_value = t._value;
	}
}
