/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/BlankView.java,v $
 * $Date: 2009/09/06 22:17:53 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class BlankView extends Composite
{
	private FlowPanel _panel = new FlowPanel();
	
	public BlankView()
    {
		initWidget(_panel);
		setStylePrimaryName("SimplePanel");
    }
}
