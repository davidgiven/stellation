/* Represents a set of asteroids.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/Asteroids.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import com.cowlark.stellation2.common.db.HasDBRepresentation;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Asteroids implements IsSerializable, Serializable, HasDBRepresentation
{
    private static final long serialVersionUID = -4437426950656249675L;
	
	public int _carboniferous;
	public int _metallic;
	
	public Asteroids()
    {
	    _carboniferous = _metallic = 0;
    }
	
	public Asteroids(int carboniferous, int metallic)
	{
		_carboniferous = carboniferous;
		_metallic = metallic;
	}
	
	public Asteroids(Asteroids other)
	{
		_carboniferous = other._carboniferous;
		_metallic = other._metallic;
	}

	public Iterable<String> toDBRepresentation()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(Integer.toString(_carboniferous));
		list.add(Integer.toString(_metallic));
	    return list;
	}
	
	public void fromDBRepresentation(Iterable<String> value)
	{
		Iterator<String> i = value.iterator();
		_carboniferous = Integer.parseInt(i.next());
		_metallic = Integer.parseInt(i.next());
	}
	
	public Object getClient()
	{
	    return this;
	}
	
	public final int getCarboniferous()
    {
    	return _carboniferous;
    }

	public final int getMetallic()
    {
    	return _metallic;
    }
}
