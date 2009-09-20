/* A custom button.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/Switch.java,v $
 * $Date: 2009/09/20 21:48:11 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;

public class Switch extends Composite
{
	private CheckBox _checkbox;

	private void init(String label, ClickHandler cb)
	{
		_checkbox = new CheckBox(label);
		if (cb != null)
			_checkbox.addClickHandler(cb);
		initWidget(_checkbox);
	}
	
	public Switch(String label, ClickHandler cb)
    {
		init(label, cb);
    }
	
	public Switch(String label)
    {
		init(label, null);
    }
	
	public void setEnabled(boolean enabled)
	{
		_checkbox.setEnabled(enabled);
	}
}
