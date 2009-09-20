/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/FactoryBuildingMonitor.java,v $
 * $Date: 2009/09/20 21:50:35 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CFactory;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FactoryBuildingMonitor extends Monitor<CFactory>
{
	public Label _label = new Label();

	public FactoryBuildingMonitor(CFactory unit)
    {
		super(unit);
    }
	
	public String getLabel()
	{
		return "Building";
	}
	
	public Widget updateImpl(CFactory factory) throws OutOfScopeException
	{
		int building = factory.getNowBuilding();
		if (building == PropertyStore.NOTHING)
			_label.setText("nothing");
		else
			_label.setText(PropertyStore.getProperties(building).getName());

		return _label;
	}
}
