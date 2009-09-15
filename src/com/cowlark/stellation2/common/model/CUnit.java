/* Client-side generic unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CUnit.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.6 $
 */


package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.client.controlpanel.UnitControlPanel;
import com.cowlark.stellation2.server.db.Property;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class CUnit extends CObject
{
	@Property
	private double _damage = 0.0;
	
	@Property
	private double _mass;
	
	public CUnit()
    {
    }
	
	public double getMass()
	{
		return _mass;
	}
	
	public double getDamage()
	{
		return _damage;
	}
	
	public CStar getStar()
	{
		return getParent().toStar();
	}
	
	public Widget createSummaryNotesField()
	{
		return new Label();
	}
	
	@Override
	public Widget createControlPanel()
	{
	    return new UnitControlPanel<CUnit>(this);
	}
}
