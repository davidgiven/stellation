/* Client-side tug.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CTug.java,v $
 * $Date: 2009/09/16 23:14:51 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.common.model;

import java.util.Iterator;
import com.cowlark.stellation2.client.controlpanel.TugControlPanel;
import com.cowlark.stellation2.client.monitors.TugCargoMonitor;
import com.cowlark.stellation2.client.view.AbstractView;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.google.gwt.user.client.ui.Widget;

public class CTug extends CShip
{
	public CTug()
    {
    }
	
	@Override
	public Properties getProperties()
	{
	    return PropertyStore.Tug;
	}
	
	public CUnit getCargo()
	{
		Iterator<CObject> i = iterator();
		if (i.hasNext())
			return (CUnit) i.next();
		return null;
	}

	@Override
	public Widget createSummaryNotesField()
	{
		return new TugCargoMonitor(this);
	}
	
	public AbstractView<? extends CObject> createControlPanel()
	{
		return new TugControlPanel(this);
	}
}
