/* Client-side generic unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CUnit.java,v $
 * $Date: 2009/09/06 22:15:12 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */


package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.server.db.Property;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class CUnit extends CObject
{
	@Property
	private Resources _maintenanceCost = new Resources();
	
	@Property
	private Resources _buildCost = new Resources();
	
	@Property
	private double _buildTime;
	
	@Property
	private double _restMass;
	
	@Property
	private double _mass;
	
	@Property
	private double _damage = 0.0;
	
	@Property
	private double _maxDamage = 100.0;
	
	public CUnit()
    {
    }
	
	public Resources getMaintenanceCost()
	{
		return _maintenanceCost;
	}
	
	public Resources getBuildCost()
	{
		return _buildCost;
	}
	
	public double getBuildTime()
	{
		return _buildTime;
	}
	
	public double getRestMass()
	{
		return _restMass;
	}
	
	public double getMass()
	{
		return _mass;
	}
	
	public double getDamage()
	{
		return _damage;
	}
	
	public double getMaxDamage()
	{
		return _maxDamage;
	}
	
	public Widget createSummaryNotesField()
	{
		return new Label();
	}
}
