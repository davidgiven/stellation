/* A simple wrapper around a Monitor. 
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/SimpleMonitorView.java,v $
 * $Date: 2009/09/20 22:15:16 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.monitors.Monitor;
import com.cowlark.stellation2.client.ui.Spacer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class SimpleMonitorView extends Composite
{
	private HorizontalPanel _panel = new HorizontalPanel();
	
	public SimpleMonitorView(Monitor<?> monitor)
    {
		_panel.add(new Label(monitor.getLabel() + ":"));
		_panel.add(new Spacer());
		_panel.add(monitor);
		
		initWidget(_panel);
    }
}
