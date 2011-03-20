package com.cowlark.stellation3.common.database.values;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import com.cowlark.stellation3.common.database.Reader;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SObject;

public class ObjectSetDatum extends Datum implements Iterable<SObject>
{
	private TreeSet<Integer> _value = new TreeSet<Integer>();
	
	Set<Integer> get()
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
			int oid = r.readInt();
			_value.add(oid);
		}
	}
	
	@Override
	public void set(Datum d)
	{
		ObjectSetDatum s = (ObjectSetDatum) d;
		_value.clear();
		_value.addAll(s._value);
	}
	
	@Override
	public Iterator<SObject> iterator()
	{
		final Iterator<Integer> i = _value.iterator(); 
		return new Iterator<SObject>()
		{
			@Override
			public boolean hasNext()
			{
			    return i.hasNext();
			}
			
			@Override
			public SObject next()
			{
			    return Game.Instance.Database.get(i.next());
			}
			
			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}
}
