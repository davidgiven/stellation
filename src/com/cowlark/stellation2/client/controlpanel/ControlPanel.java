/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/Attic/ControlPanel.java,v $
 * $Date: 2009/09/08 23:01:28 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.monitors.UnitBannerMonitor;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.ui.DataTable;
import com.cowlark.stellation2.client.view.AbstractView;
import com.cowlark.stellation2.common.model.CObject;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class ControlPanel<T extends CObject> extends AbstractView<T>
{
	private ScrollPanel _scrollpane = new ScrollPanel();
	private FlowPanel _panel = new FlowPanel();
	private DataTable _data = new DataTable();
	
	public ControlPanel(T object)
    {
		super(object);
		
		initWidget(_scrollpane);
		_scrollpane.setAlwaysShowScrollBars(false);
		_scrollpane.add(_panel);
		
		setStylePrimaryName("SimplePanel");
		_panel.setStylePrimaryName("PanelInner");
		
		add(new UnitBannerMonitor(object));
		add(_data);
    }
	
	public void add(Widget w)
    {
	    _panel.add(w);
    }
	
	public DataGroup addGroup()
	{
		return _data.addGroup();
	}
	
	@Override
	public void onChange(ChangeCallback cb)
	{
	}
}
