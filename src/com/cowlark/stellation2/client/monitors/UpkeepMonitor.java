/* Monitors resources in any HasResources object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/UpkeepMonitor.java,v $
 * $Date: 2009/09/15 23:14:36 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UpkeepMonitor extends Monitor<CUnit>
{
	private Label _label = new Label();
	
	public UpkeepMonitor(CUnit unit)
    {
		super(unit);
    }
	
	public String getLabel()
	{
		return "Upkeep /hr";
	}
	
	public Widget updateImpl(CUnit unit) throws OutOfScopeException
	{
		Resources r = unit.getProperties().getMaintenanceCost();

		StringBuilder sb = new StringBuilder();
		sb.append("A: ");
		sb.append(r.getAntimatter());
		sb.append(" M: ");
		sb.append(r.getMetal());
		sb.append(" O: ");
		sb.append(r.getOrganics());

		_label.setText(sb.toString());
		return _label;
	}
}
