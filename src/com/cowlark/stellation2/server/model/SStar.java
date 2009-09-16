/* Server-side star.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SStar.java,v $
 * $Date: 2009/09/16 23:13:12 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.server.model;

import java.util.HashSet;
import com.cowlark.stellation2.common.Asteroids;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.exceptions.InvalidObjectException;
import com.cowlark.stellation2.common.exceptions.ResourcesNotAvailableException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.common.model.CStar;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CStar.class)
public class SStar extends SObject
{
    private static final long serialVersionUID = 6945033077554941829L;

	@Property(scope = S.GLOBAL)
    private String _name;
    
    @Property(scope = S.GLOBAL)
    private double _x, _y;
    
    @Property(scope = S.GLOBAL)
    private double _brightness;
    
    @Property(scope = S.LOCAL)
    private Resources _resources = new Resources();
    
    @Property(scope = S.LOCAL)
    private Asteroids _asteroids = new Asteroids();
    
    public SStar initialise(String name, double x, double y, double brightness,
    		Resources resources, Asteroids asteroids)
    {
    	super.initialise();
    	
    	_name = name;
    	_x = x;
    	_y = y;
    	
    	_brightness = brightness;
    	
    	if (resources != null)
    		_resources = resources;

    	if (asteroids != null)
    		_asteroids = asteroids;
    	
    	return this;
    }
    
    public SStar toStar()
    {
    	return this;
    }
    
    public String getName()
    {
    	return _name;
    }

	public double getBrightness()
    {
    	return _brightness;
    }
	
	public Resources getResources()
    {
	    return _resources;
    }
	
	public void setResources(Resources resources)
    {
	    _resources = resources;
	    dirty();
    }
	
	public void destroyResources(Resources r) throws ResourcesNotAvailableException
	{
		double antimatter = _resources.getAntimatter() - r.getAntimatter();
		double metal = _resources.getMetal() - r.getMetal();
		double organics = _resources.getOrganics() - r.getOrganics();
		
		if ((antimatter < 0.0) || (metal < 0.0) || (organics < 0.0))
			throw new ResourcesNotAvailableException();
		
		setResources(new Resources(antimatter, metal, organics));
	}
	
	@Override
	public void consume(Resources r) throws ResourcesNotAvailableException
	{
		destroyResources(r);
	}
	
	public void checkObjectVisibleTo(SPlayer player)
		throws StellationException
	{
		/* If any fleet in this system is owned by the player, and that fleet
		 * has a jumpship, the fleet is visible. */
		
		for (SObject o : this)
		{
			SFleet fleet = o.toFleet();
			if ((fleet != null) && (fleet.getOwner() == player) &&
				(fleet.getJumpshipCount() > 0))
			{
				return;
			}
		}
		
		/* The object isn't visible! */
		
		throw new InvalidObjectException(getId());
	}
	
    public SStar log(String message)
    {
    	/* Send to all players with a presence in this system. */
    	
    	HashSet<SPlayer> players = new HashSet<SPlayer>();
    	for (SObject o : this)
    	{
			SFleet fleet = o.toFleet();
			if ((fleet != null) &&
				(fleet.getJumpshipCount() > 0))
			{
				players.add(fleet.getOwner());
			}
    	}
    	
    	for (SPlayer player : players)
    		player.log(message);
    	
    	return this;
    }	
}
