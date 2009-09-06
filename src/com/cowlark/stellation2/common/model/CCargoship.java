/* Client-side cargoship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CCargoship.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.client.monitors.ResourcesMonitor;
import com.cowlark.stellation2.common.HasResources;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CCargoship extends CShip implements HasResources
{
	@Property
	private Resources _cargo = new Resources();

	public CCargoship()
    {
    }

	@Override
	public String getShortName()
	{
	    return "Cargoship";
	}
	
	@Override
	public String getDescription()
	{
		return "Cargo ships can carry large quantities of the three main " +
			"commodities. They can be used to transfer resources from one " +
			"star system to another, and are also used to supply vessels in " +
			"operation.";
	}

	@Override
	public Widget createSummaryNotesField()
	{
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.add(new Label("Carrying: "));
		hpanel.add(new ResourcesMonitor(this));
		hpanel.add(new Button("Unload"));
		return hpanel;
	}
	
	public Resources getCargo() throws OutOfScopeException
    {
		checkOwner();
    	return _cargo;
    }
	
	public Resources getResources() throws OutOfScopeException
	{
	    return getCargo();
	}
}
