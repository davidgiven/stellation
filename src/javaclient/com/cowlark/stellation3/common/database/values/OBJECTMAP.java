package com.cowlark.stellation3.common.database.values;

import java.util.HashMap;
import java.util.Map;
import com.cowlark.stellation3.common.database.Reader;

public class OBJECTMAP extends DATUM
{
	private HashMap<String, Integer> _value = new HashMap<String, Integer>();
	
	Map<String, Integer> get()
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
			String key = r.readString();
			int oid = r.readInt();
			_value.put(key, oid);
		}
	}
	
	@Override
	public void set(DATUM d)
	{
		OBJECTMAP s = (OBJECTMAP) d;
		_value.clear();
		_value.putAll(s._value);
	}	
}
