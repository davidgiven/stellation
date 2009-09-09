/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/UnitNameMonitor.java,v $
 * $Date: 2009/09/09 23:18:13 $
 * $Author: dtrg $
 * $Revision: 1.5 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UnitNameMonitor extends Monitor<CUnit> implements ClickHandler
{
	public Label _label = new Label();

	public UnitNameMonitor(CUnit unit)
    {
		super(unit);
		_label.addStyleName("Link");
		_label.addClickHandler(this);
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
	
	public void onClick(ClickEvent event)
	{
		try
		{
			CUnit unit = getObject();
			Widget view = unit.createControlPanel();
			Stellation2.showRightPaneView(view);
		}
		catch (OutOfScopeException e)
		{
		}
	}
}
