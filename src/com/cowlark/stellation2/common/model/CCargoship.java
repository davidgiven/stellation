/* Client-side cargoship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CCargoship.java,v $
 * $Date: 2009/09/14 22:15:34 $
 * $Author: dtrg $
 * $Revision: 1.5 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.client.controlpanel.CargoshipControlPanel;
import com.cowlark.stellation2.client.monitors.ResourcesMonitor;
import com.cowlark.stellation2.client.view.AbstractView;
import com.cowlark.stellation2.common.HasResources;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;
import com.google.gwt.user.client.ui.Widget;

public class CCargoship extends CShip implements HasResources
{
	@Property
	private Resources _cargo = new Resources();

	public CCargoship()
    {
    }

	@Override
	public Properties getProperties()
	{
	    return PropertyStore.Cargoship;
	}
	
	@Override
	public Widget createSummaryNotesField()
	{
		return new ResourcesMonitor(this);
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
	
	public AbstractView<? extends CObject> createControlPanel()
	{
		return new CargoshipControlPanel(this);
	}
}
