/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/Attic/UnitBannerMonitor.java,v $
 * $Date: 2009/09/08 23:01:28 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CObject;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UnitBannerMonitor extends Monitor<CObject>
{
	public FlowPanel _panel = new FlowPanel();
	public HorizontalPanel _title = new HorizontalPanel();

	public UnitBannerMonitor(CObject object)
    {
		super(object);
		_panel.add(_title);
		
		Properties props = object.getProperties();
		
		Label label = new Label(props.getName());
		label.addStyleName("UnitBannerTitle");
		_title.add(label);
		label = new Label("#"+object.getId());
		label.addStyleName("UnitBannerObjectId");
		_title.add(label);
		
		HTML html = new HTML(props.getDescription());
		html.addStyleName("UnitBannerDescription");
		_panel.add(html);
    }
	
	public String getLabel()
	{
		return "Unit";
	}
	
	public Widget updateImpl(CObject object) throws OutOfScopeException
	{
		return _panel;
	}
}
