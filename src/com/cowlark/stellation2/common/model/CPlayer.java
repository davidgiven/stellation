/* Client-side player.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CPlayer.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import java.util.Set;
import java.util.TreeSet;
import com.cowlark.stellation2.common.db.MapOfClientObjects;
import com.cowlark.stellation2.server.db.Property;

public class CPlayer extends CObject
{
	@Property
	private String _name;
	
	@Property
	private String _empire;
	
	@Property
	private String _uid;
	
	@Property
	private MapOfClientObjects _fleets = new MapOfClientObjects();
	
	@Property
	private boolean _administrator;
	
	public CPlayer()
    {
    }
	
	public CPlayer toPlayer()
	{
		return this;
	}
	
	public String getName()
    {
	    return _name;
    }
	
	public String getEmpire()
    {
	    return _empire;
    }
	
	public String getUid()
    {
	    return _uid;
    }
	
	public MapOfClientObjects getFleets()
    {
	    return _fleets;
    }
	
	public boolean isAdministrator()
    {
	    return _administrator;
    }
	
	/* Returns a vector of all star systems visible to the player. */
	
	public Set<CStar> getVisibleStarsystems()
	{
		Set<CStar> set = new TreeSet<CStar>();
		for (CObject o : _fleets.values())
		{
			CFleet fleet = o.toFleet();
			if (fleet != null)
			{
				CStar star = (CStar) fleet.getParent();
				set.add(star);
			}
		}

		return set;
	}
}
