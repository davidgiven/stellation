/* A simple wrapper around a Monitor. 
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/SimpleMonitorView.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.monitors.Monitor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class SimpleMonitorView extends Composite
{
	private HorizontalPanel _panel = new HorizontalPanel();
	
	public SimpleMonitorView(Monitor<?> monitor)
    {
		_panel.setWidth("100%");
		_panel.add(new Label(monitor.getLabel() + ":"));
		_panel.add(monitor);
		
		initWidget(_panel);
    }
}
