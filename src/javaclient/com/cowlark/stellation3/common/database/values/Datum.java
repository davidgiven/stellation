package com.cowlark.stellation3.common.database.values;

import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.database.Reader;

public abstract class Datum
{
	public static Datum create(Hash type)
	{
		switch (type)
		{
			case Number: return new NumberDatum();
			case Object: return new ObjectDatum();
			case ObjectMap: return new ObjectMapDatum();
			case ObjectSet: return new ObjectSetDatum();
			case String: return new StringDatum();
			case Token: return new TokenDatum();
		}
		return null;
	}
	
	public abstract void set(Reader r);
	public abstract void set(Datum d);
}
