package com.cowlark.stellation3.common.database;

public class ArrayReader extends Reader
{
	private String[] _data;
	private int _position;
	
	public ArrayReader(String[] data, int position)
	{
		_data = data;
		_position = position;
	}
	
	public ArrayReader(String[] data)
	{
		this(data, 0);
	}
	
	public boolean isEOF()
	{
		return (_position == _data.length);
	}
	
	public String readString()
	{
		assert(!isEOF());
		return _data[_position++];
	}
}
