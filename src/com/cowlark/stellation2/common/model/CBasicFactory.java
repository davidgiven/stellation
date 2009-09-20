/* Client-side generic ship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CBasicFactory.java,v $
 * $Date: 2009/09/20 21:50:35 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.client.controlpanel.BasicFactoryControlPanel;
import com.cowlark.stellation2.client.monitors.FactoryBuildingMonitor;
import com.cowlark.stellation2.client.view.AbstractView;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.google.gwt.user.client.ui.Widget;

public class CBasicFactory extends CFactory
{
	public CBasicFactory()
    {
    }
	
	@Override
	public Properties getProperties()
	{
		return PropertyStore.BasicFactory;
	}
	
	@Override
	public Widget createSummaryNotesField()
	{
		return new FactoryBuildingMonitor(this);
	}
	
	public AbstractView<? extends CObject> createControlPanel()
	{
		return new BasicFactoryControlPanel(this);
	}
}
