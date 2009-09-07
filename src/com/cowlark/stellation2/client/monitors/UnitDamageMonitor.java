/* Monitors the amount of damage in a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/UnitDamageMonitor.java,v $
 * $Date: 2009/09/07 22:28:13 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.client.ui.LayeredPanel;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UnitDamageMonitor extends Monitor<CUnit>
{
	public Label _label = new Label();

	public UnitDamageMonitor(CUnit unit)
    {
		super(unit);
    }
	
	public String getLabel()
	{
		return "Damage";
	}
	
	public Widget updateImpl(CUnit unit) throws OutOfScopeException
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(unit.getDamage());
		sb.append(" / ");
		sb.append(unit.getMaxDamage());
		
		_label.setText(sb.toString());
		return _label;
	}
}
