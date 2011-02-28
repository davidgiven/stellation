package com.cowlark.stellation3.common.database.values;

import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.database.Reader;

public abstract class DATUM
{
	public static DATUM create(Hash type)
	{
		switch (type)
		{
			case Number: return new NUMBER();
			case Object: return new OBJECT();
			case ObjectMap: return new OBJECTMAP();
			case ObjectSet: return new OBJECTSET();
			case String: return new STRING();
			case Token: return new TOKEN();
		}
		return null;
	}
	
	public abstract void set(Reader r);
	public abstract void set(DATUM d);
}
