package com.cowlark.stellation3.common.database;

public class Reader
{
	private String[] _strings;
	private int _position;
		
	public Reader(String[] strings, int position)
	{
		_strings = strings;
		_position = position;
	}
	
	public Reader(String[] strings)
	{
		this(strings, 0);
	}
	
	public boolean isEOF()
	{
		return (_position == _strings.length);
	}
	
	public String readString()
	{
		assert(!isEOF());
		return _strings[_position++];
	}
	
	public int readInt()
	{
		return Integer.parseInt(readString());
	}
	
	public double readDouble()
	{
		return Double.parseDouble(readString());
	}
	
	public Hash readHash()
	{
		return Hash.valueOf(readString());
	}
}
