package com.cowlark.stellation3.common.database.values;

import java.util.HashSet;
import java.util.Set;
import com.cowlark.stellation3.common.database.Reader;

public class OBJECTSET extends DATUM
{
	private HashSet<Integer> _value = new HashSet<Integer>();
	
	Set<Integer> get()
	{
		return _value;
	}
	
	@Override
	public void set(Reader r)
	{
		_value.clear();
		int count = r.readInt();
		for (int i = 0; i < count; i++)
		{
			int oid = r.readInt();
			_value.add(oid);
		}
	}
	
	@Override
	public void set(DATUM d)
	{
		OBJECTSET s = (OBJECTSET) d;
		_value.clear();
		_value.addAll(s._value);
	}	
}
