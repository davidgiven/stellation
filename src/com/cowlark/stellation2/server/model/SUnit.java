/* Server-side generc unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SUnit.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.ResourcesNotAvailableException;
import com.cowlark.stellation2.common.model.CUnit;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CUnit.class)
public abstract class SUnit extends SObject
{
    private static final long serialVersionUID = 6264037018308814748L;

	@Property(scope = S.LOCAL)
	private double _damage;
	
	@Property
	private DBRef<STimer> _maintenanceTimer = DBRef.NULL();
	
	@Override
	public SUnit initialise()
	{
	    super.initialise();
	    _damage = 0.0;
	    return this;
	}
	
	public double getDamage()
	{
		return _damage;
	}
	
	public SUnit setDamage(double damage)
	{
		_damage = damage;
		dirty();
		return this;
	}
	
	public abstract Resources getMaintenanceCost();
	public abstract Resources getBuildCost();
	public abstract double getBuildTime();
	public abstract double getRestMass();
	public abstract double getMaxDamage();
	
	public double getMass()
	{
		return getRestMass();
	}
	
	@Override
	public long timerExpiry(STimer timer)
	{
		if (timer.equals(_maintenanceTimer))
		{
			Resources r = getMaintenanceCost();
			try
			{
				consume(r);
				return S.TICK;
			}
			catch (ResourcesNotAvailableException e)
			{
				log("unit starves!");
				starve();
				return S.CANCELTIMER;
			}
		}
		else
			return super.timerExpiry(timer);
	}
	
	public SUnit makeAlive()
	{
		STimer t = new STimer()
			.initialise(S.TICK, this);
		_maintenanceTimer = new DBRef<STimer>(t);
		return this;
	}
	
	public SUnit makeDead()
	{
		STimer t = _maintenanceTimer.get();
		if (t != null)
			t.cancel();
		_maintenanceTimer = DBRef.NULL();
		return this;
	}
	
	@Override
	public void destroy()
	{
		makeDead();
	    super.destroy();
	}
	
	@Override
	public void consume(Resources r) throws ResourcesNotAvailableException
	{
		if ((r.getAntimatter() == 0.0) &&
			(r.getMetal() == 0.0) &&
			(r.getOrganics() == 0.0))
			return;
		
		getLocation().consume(r);
	}

	public void starve()
	{
		destroy();
	}
	
	public SUnit log(String message)
	{
		SFleet fleet = getLocation().toFleet();
		if (fleet != null)
			fleet.log(message);
		return this;
	}
}
