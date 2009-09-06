/* Monitors a unit's summary notes field.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/UnitNotesMonitor.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class UnitNotesMonitor extends Monitor<CUnit>
{
	public SimplePanel _panel = new SimplePanel();

	public UnitNotesMonitor(CUnit unit)
    {
		super(unit);
    }
	
	public String getLabel()
	{
		return "Notes";
	}
	
	public Widget updateImpl(CUnit unit) throws OutOfScopeException
	{
		Widget w = unit.createSummaryNotesField();
		if (w != _panel.getWidget())
		{
			_panel.clear();
			_panel.add(w);
		}
		return _panel;
	}
}
