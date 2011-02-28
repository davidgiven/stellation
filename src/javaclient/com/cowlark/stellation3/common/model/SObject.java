package com.cowlark.stellation3.common.model;

import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.database.SBase;

public class SObject extends SBase
{
	public static SObject create(int oid, Hash type)
	{
		switch (type)
		{
			case SObject:    return new SObject(oid);
			default:         assert(false);
		}
		
		return null;
	}
	
	private int _oid;
	
	public SObject(int oid)
	{
		_oid = oid;
	}
	
	public void objectChanged()
	{
	}
}
