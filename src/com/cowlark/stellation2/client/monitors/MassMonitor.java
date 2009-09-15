/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/MassMonitor.java,v $
 * $Date: 2009/09/15 23:14:36 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MassMonitor extends Monitor<CUnit>
{
	public Label _label = new Label();

	public MassMonitor(CUnit unit)
    {
		super(unit);
    }
	
	public String getLabel()
	{
		return "Mass";
	}
	
	public Widget updateImpl(CUnit unit) throws OutOfScopeException
	{
		double mass = unit.getMass();
		
		String s;
		if (Double.isInfinite(mass))
			s = "huge";
		else
			s = S.MASS_FORMAT.format(mass) + " tonnes";

		_label.setText(s);
		return _label;
	}
}
