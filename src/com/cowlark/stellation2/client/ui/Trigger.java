/* A custom button.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/Trigger.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

public class Trigger extends Composite
{
	private Button _button;

	private void init(String label, ClickHandler cb)
	{
		_button = new Button(label);
		if (cb != null)
			_button.addClickHandler(cb);
		initWidget(_button);
	}
	
	public Trigger(String label, ClickHandler cb)
    {
		init(label, cb);
    }
	
	public Trigger(String label)
    {
		init(label, null);
    }
	
	public void setEnabled(boolean enabled)
	{
		_button.setEnabled(enabled);
	}
}
