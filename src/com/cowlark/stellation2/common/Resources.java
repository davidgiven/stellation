/* A set of resource data.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/Resources.java,v $
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

public final class Resources implements IsSerializable, Serializable,
		HasDBRepresentation
{
    private static final long serialVersionUID = 4099261119581834069L;
    
	public double _antimatter;
	public double _metal;
	public double _organics;
	
	public Resources()
    {
	    _antimatter = _metal = _organics = 0.0;
    }
	
	public Resources(double a, double m, double o)
	{
		_antimatter = a;
		_metal = m;
		_organics = o;
	}
	
	public Resources(Resources other)
	{
		_antimatter = other._antimatter;
		_metal = other._metal;
		_organics = other._organics;
	}

	public Iterable<String> toDBRepresentation()
	{
		LinkedList<String> list = new LinkedList<String>();
		list.add(Double.toString(_antimatter));
		list.add(Double.toString(_metal));
		list.add(Double.toString(_organics));
	    return list;
	}
	
	public void fromDBRepresentation(Iterable<String> value)
	{
		Iterator<String> i = value.iterator();
		_antimatter = Double.parseDouble(i.next());
		_metal = Double.parseDouble(i.next());
		_organics = Double.parseDouble(i.next());
	}
		
	public Object getClient()
	{
	    return this;
	}
	
	public final double getOrganics()
    {
    	return _organics;
    }

	public final double getAntimatter()
    {
    	return _antimatter;
    }

	public final double getMetal()
    {
    	return _metal;
    }
}
