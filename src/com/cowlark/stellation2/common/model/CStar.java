/* Client-side generic star.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CStar.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.Asteroids;
import com.cowlark.stellation2.common.HasResources;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;

public class CStar extends CObject implements Comparable<CStar>, HasResources
{
	@Property
    private String _name;
	
	@Property
    private double _x;
	
	@Property
    private double _y;
	
	@Property
    private double _brightness;
	
	@Property
    private Resources _resources = new Resources();
	
	@Property
    private Asteroids _asteroids = new Asteroids();
    	
	public CStar()
    {
    }
	
	public CStar toStar()
	{
		return this;
	}
	
	public String getName()
    {
	    return _name;
    }
	
	public double getX()
    {
	    return _x;
    }
	
	public double getY()
    {
	    return _y;
    }
	
	public double getBrightness()
    {
	    return _brightness;
    }
	
	public Resources getResources() throws OutOfScopeException
    {
		checkNear();
    	return _resources;
    }

	public Asteroids getAsteroids() throws OutOfScopeException
    {
		checkNear();
    	return _asteroids;
    }

	public int compareTo(CStar other)
    {
	    return _name.compareTo(other.getName());
    }
}
