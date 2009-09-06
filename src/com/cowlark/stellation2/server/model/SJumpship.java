/* Server-side jumpship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SJumpship.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.model.CJumpship;
import com.cowlark.stellation2.server.db.CClass;

@CClass(name = CJumpship.class)
public class SJumpship extends SShip
{
    private static final long serialVersionUID = -4568269796382604197L;
    
	private static double REST_MASS = 5000.0;
    private static Resources BUILD_COST = new Resources(20000.0, 1000.0, 10000.0);
    private static double BUILD_TIME = 5.0;
    private static Resources MAINTENANCE_COST = new Resources(5.0, 2.0, 0.0);
    private static double MAX_DAMAGE = 1000.0;

    public SJumpship initialise()
    {
        super.initialise();
        return this;
    }
    
	@Override public double getRestMass() { return REST_MASS; }
	@Override public Resources getBuildCost() { return BUILD_COST; }
	@Override public double getBuildTime() { return BUILD_TIME; }
	@Override public Resources getMaintenanceCost() { return MAINTENANCE_COST; }
	@Override public double getMaxDamage() { return MAX_DAMAGE; }
	
	@Override
	public void onAdditionTo(SObject parent)
	{
	    super.onAdditionTo(parent);
	    SFleet fleet = parent.toFleet();
	    if (fleet != null)
	    	fleet.setJumpshipCount(fleet.getJumpshipCount() + 1);
	}
	
	@Override
	public void onRemovalFrom(SObject parent)
	{
	    SFleet fleet = parent.toFleet();
	    if (fleet != null)
	    	fleet.setJumpshipCount(fleet.getJumpshipCount() - 1);
	    super.onRemovalFrom(parent);
	}
}
