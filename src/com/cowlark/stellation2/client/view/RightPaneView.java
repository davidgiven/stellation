/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/Attic/RightPaneView.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class RightPaneView extends Composite
{
	private FlowPanel _panel = new FlowPanel();
	
	public RightPaneView()
    {
		initWidget(_panel);
		setStylePrimaryName("SimplePanel");
		
		_panel.add(new Label("Pane!"));
    }
}
