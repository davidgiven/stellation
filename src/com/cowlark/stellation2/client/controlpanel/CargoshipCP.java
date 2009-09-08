/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/Attic/CargoshipCP.java,v $
 * $Date: 2009/09/08 23:01:28 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.view.FullWidthMonitorRow;
import com.cowlark.stellation2.common.model.CCargoship;

public class CargoshipCP extends ControlPanel<CCargoship>
{
	private DataGroup _group;
	
	public CargoshipCP(CCargoship ship)
    {
		super(ship);
		
		_group = addGroup();
		_group.add(new FullWidthMonitorRow(new CargoshipResourcesMonitor(ship)));
    }
}
