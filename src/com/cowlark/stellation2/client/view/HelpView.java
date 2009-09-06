/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/HelpView.java,v $
 * $Date: 2009/09/06 22:15:34 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;

public class HelpView extends Composite
{
	private Frame _panel = new Frame();
	
	public HelpView()
    {
		initWidget(_panel);
		setStylePrimaryName("SimplePanel");

		setHelp("welcome.html");
    }
	
	public void setHelp(String url)
	{
		_panel.setUrl("help/"+url);
	}
}
