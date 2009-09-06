/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/Attic/WelcomeView.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;

public class WelcomeView extends Composite
{
	private ScrollPanel _scrollpanel = new ScrollPanel();
	private FlowPanel _panel = new FlowPanel();
	
	public WelcomeView()
    {
		initWidget(_scrollpanel);
		setStylePrimaryName("SimplePanel");
		
		_scrollpanel.setAlwaysShowScrollBars(false);
		_scrollpanel.add(_panel);
		
		_panel.setStylePrimaryName("PanelInner");
		
		_panel.add(new HTML("Welcome to Stellation!"));
    }
}
