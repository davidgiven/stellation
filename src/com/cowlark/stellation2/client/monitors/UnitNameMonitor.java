/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/UnitNameMonitor.java,v $
 * $Date: 2009/09/06 22:15:12 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UnitNameMonitor extends Monitor<CUnit>
{
	public Label _label = new Label();

	public UnitNameMonitor(CUnit unit)
    {
		super(unit);
    }
	
	public String getLabel()
	{
		return "Name";
	}
	
	public Widget updateImpl(CUnit unit) throws OutOfScopeException
	{
		Properties props = unit.getProperties();
		_label.setText(props.getName());
		return _label;
	}
}
