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
			case SUniverse:  return new SUniverse(oid);
			case SGalaxy:    return new SGalaxy(oid);
			case SStar:      return new SStar(oid);
			case SPlayer:    return new SPlayer(oid);
			case SFleet:     return new SFleet(oid);
			case SUnit:      return new SUnit(oid);
			case SJumpship:  return new SJumpship(oid);
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
