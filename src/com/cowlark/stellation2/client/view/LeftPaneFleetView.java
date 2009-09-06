/* Shows a Fleet in the left pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/LeftPaneFleetView.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CFleet;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class LeftPaneFleetView extends AbstractView<CFleet>
{
	private SimplePanel _panel = new SimplePanel();
	private Label _fleetName = new Label();
	
	public LeftPaneFleetView(CFleet object)
    {
		super(object);
		initWidget(_panel);
		
		_panel.add(_fleetName);
    }

	@Override
	public void onChange(ChangeCallback cb)
	{
		String s;
		
		try
		{
			s = getObject().getName();
		}
		catch (OutOfScopeException e)
		{
			s = "(unavailable)";
		}

		_fleetName.setText(s);
	}	
}
