package com.cowlark.stellation3.common.database;

import java.util.HashMap;
import java.util.HashSet;
import com.cowlark.stellation3.common.database.values.Datum;
import com.cowlark.stellation3.common.database.values.ObjectDatum;
import com.cowlark.stellation3.common.database.values.TokenDatum;
import com.cowlark.stellation3.common.model.SObject;

public class Database
{
	@SuppressWarnings("serial")
    private static class UnknownObject extends HashMap<Hash, Datum>
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
	
	private HashMap<Integer, SObject> _data =
		new HashMap<Integer, SObject>();
	
	public SObject get(int oid)
	{
		return _data.get(oid);
	}
	
	public SObject get(ObjectDatum o)
	{
		return get(o.getOid());
	}
	
	public void loadBatch(Reader reader)
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
				Datum datum = object.getDatumByName(kid);
				if (datum == null)
				{
					datum = Datum.create(SObject.getDatumTypeByName(kid));
					object.setDatumByName(kid, datum);
				}
				datum.set(reader);
				changedObjects.add(object);
			}
			else if (kid == Hash.Class)
			{
				TokenDatum classtoken = new TokenDatum();
				classtoken.set(reader);
				
				object = SObject.create(oid, classtoken.get());
				object.setDatumByName(Hash.Class, classtoken);
				_data.put(oid, object);
				
				UnknownObject uo = pending.get(oid);
				if (uo != null)
				{
					for (Hash k : uo.keySet())
					{
						Datum v = uo.get(k);
						object.setDatumByName(k, v);
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
				
				Datum datum = Datum.create(SBase.getDatumTypeByName(kid));
				datum.set(reader);
				
				uo.put(kid, datum);
			}
		}
		
		assert(pending.isEmpty());
		
		for (SObject object : changedObjects)
			object.objectChanged();
	}
}
