package com.cowlark.stellation3.common.database;

import java.util.HashMap;
import java.util.HashSet;
import com.cowlark.stellation3.common.database.values.DATUM;
import com.cowlark.stellation3.common.database.values.OBJECT;
import com.cowlark.stellation3.common.database.values.TOKEN;
import com.cowlark.stellation3.common.model.SObject;

public class Database
{
	@SuppressWarnings("serial")
    private static class UnknownObject extends HashMap<Hash, DATUM>
	{
	}
	
	public static class Key
	{
		public final int oid;
		public final Hash kid;
		
		public Key(int oid, Hash kid)
        {
			this.oid = oid;
			this.kid = kid;
        }
		
		@Override
		public int hashCode()
		{
			return oid * kid.hashCode();
		}
		
		@Override
		public boolean equals(Object other)
		{
			if (other instanceof Key)
			{
				Key op = (Key) other;
				return (op.oid == oid) && (op.kid == kid);
			}
			return false;
		}
	}
	
	private static HashMap<Integer, SObject> _data =
		new HashMap<Integer, SObject>();
	
	public static SObject get(int oid)
	{
		return _data.get(oid);
	}
	
	public static SObject get(OBJECT o)
	{
		return Database.get(o.getOid());
	}
	
	public static void loadBatch(Reader reader)
	{
		HashMap<Integer, UnknownObject> pending =
			new HashMap<Integer, UnknownObject>();
		HashSet<SObject> changedObjects = new HashSet<SObject>();
		
		while (!reader.isEOF())
		{
			int oid = reader.readInt();
			Hash kid = reader.readHash();
			
			SObject object = _data.get(oid);
			if (object != null)
			{
				reader.readHash(); // discard type
				object.getDatumByName(kid).set(reader);
				changedObjects.add(object);
			}
			else if (kid == Hash.Class)
			{
				reader.readHash(); // discard type
				TOKEN classtoken = new TOKEN();
				classtoken.set(reader);
				
				object = SObject.create(oid, classtoken.get());
				object.Class.set(classtoken);
				_data.put(oid, object);
				
				UnknownObject uo = pending.get(oid);
				if (uo != null)
				{
					for (Hash k : uo.keySet())
					{
						DATUM v = uo.get(k);
						object.getDatumByName(k).set(v);
					}
					
					pending.remove(oid);
				}
				
				changedObjects.add(object);
			}
			else
			{
				UnknownObject uo = pending.get(oid);
				if (uo == null)
				{
					uo = new UnknownObject();
					pending.put(oid, uo);
				}
				
				DATUM datum = DATUM.create(reader.readHash());
				datum.set(reader);
				
				uo.put(kid, datum);
			}
		}
		
		assert(pending.isEmpty());
		
		for (SObject object : changedObjects)
			object.objectChanged();
	}
}
