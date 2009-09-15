/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/OwnerMonitor.java,v $
 * $Date: 2009/09/15 23:14:36 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CPlayer;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OwnerMonitor extends Monitor<CUnit> implements ClickHandler
{
	public Label _label = new Label();

	public OwnerMonitor(CUnit unit)
    {
		super(unit);
		_label.addStyleName("Link");
		_label.addClickHandler(this);
    }
	
	public String getLabel()
	{
		return "Owner";
	}
	
	public Widget updateImpl(CUnit unit) throws OutOfScopeException
	{
		CPlayer player = unit.getOwner();
		_label.setText(player.getEmpire());
		return _label;
	}
	
	public void onClick(ClickEvent event)
	{
		try
		{
			CUnit unit = getObject();
		}
		catch (OutOfScopeException e)
		{
		}
	}
}
