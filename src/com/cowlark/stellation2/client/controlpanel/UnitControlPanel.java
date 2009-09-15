/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/UnitControlPanel.java,v $
 * $Date: 2009/09/15 23:14:36 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.monitors.MassMonitor;
import com.cowlark.stellation2.client.monitors.OwnerMonitor;
import com.cowlark.stellation2.client.monitors.UnitDamageMonitor;
import com.cowlark.stellation2.client.monitors.UpkeepMonitor;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.view.TabularMonitorRow;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class UnitControlPanel<T extends CUnit> extends AbstractControlPanel<T>
{
	private Label _mass = new Label();
	private SimplePanel _upkeep = new SimplePanel();
	
	public UnitControlPanel(T object)
	{
		super(object);
		
		DataGroup group = addGroup();
		group.add(new TabularMonitorRow(
				new OwnerMonitor(object)));
		group.add(new TabularMonitorRow(
				new MassMonitor(object)));
		group.add(new TabularMonitorRow(
				new UpkeepMonitor(object)));
		group.add(new TabularMonitorRow(
				new UnitDamageMonitor(object)));
	}
}
