/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/FleetNameMonitor.java,v $
 * $Date: 2009/09/07 21:48:10 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.client.ui.WrappedLabel;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CFleet;
import com.google.gwt.user.client.ui.Widget;

public class FleetNameMonitor extends Monitor<CFleet>
{
	public WrappedLabel _label = new WrappedLabel();

	public FleetNameMonitor(CFleet fleet)
    {
		super(fleet);
    }
	
	public String getLabel()
	{
		return "Name";
	}
	
	public Widget updateImpl(CFleet fleet) throws OutOfScopeException
	{
		_label.setText(fleet.getName());
		return _label;
	}
}
