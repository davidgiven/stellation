package com.cowlark.stellation3.common.database;

public abstract class Reader
{
	public abstract boolean isEOF();
	public abstract String readString();
	
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
