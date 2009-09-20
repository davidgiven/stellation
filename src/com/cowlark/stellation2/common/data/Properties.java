/* The overall game screen.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/data/Properties.java,v $
 * $Date: 2009/09/20 21:47:14 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.common.data;

import java.util.HashSet;
import java.util.Set;
import com.cowlark.stellation2.common.Resources;

public class Properties
{
	private String _name = "Unnamed Object";
	private String _description = "This object has no description yet.";
	private double _mass = 0.0;
	private double _maxDamage;
	private Resources _maintenanceCost = null;
	private Resources _buildCost = null;
	private double _buildTime;
	private Set<Integer> _buildable;
	private boolean _static = false;
	
	public Properties()
    {
    }
	
	public String getName()
    {
	    return _name;
    }
	
	public Properties setName(String name)
    {
	    _name = name;
	    return this;
    }
	
	public String getDescription()
    {
	    return _description;
    }
	
	public Properties setDescription(String description)
    {
	    _description = description;
	    return this;
    }
	
	public double getMass()
    {
	    return _mass;
    }
	
	public Properties setMass(double mass)
    {
	    _mass = mass;
	    return this;
    }
	
	public double getMaxDamage()
    {
	    return _maxDamage;
    }
	
	public Properties setMaxDamage(double maxDamage)
    {
	    _maxDamage = maxDamage;
	    return this;
    }
	
	public Resources getMaintenanceCost()
    {
	    return _maintenanceCost;
    }
	
	public Properties setMaintenanceCost(Resources maintenanceCost)
    {
	    _maintenanceCost = maintenanceCost;
	    return this;
    }

	public Resources getBuildCost()
    {
	    return _buildCost;
    }
	
	public Properties setBuildCost(Resources buildCost)
    {
	    _buildCost = buildCost;
	    return this;
    }
	
	public double getBuildTime()
	{
		return _buildTime;
	}
	
	public Properties setBuildTime(double buildTime)
	{
		_buildTime = buildTime;
		return this;
	}
	
	public Set<Integer> getBuildable()
    {
	    return _buildable;
    }
	
	public Properties setBuildable(Integer... objects)
    {
		_buildable = new HashSet<Integer>();
		for (int o : objects)
			_buildable.add(o);
		return this;
    }
	
	public boolean isStatic()
    {
	    return _static;
    }
	
	public Properties setStatic(boolean s)
    {
	    _static = s;
	    return this;
    }
}
