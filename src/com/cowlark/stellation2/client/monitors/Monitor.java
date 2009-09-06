/* Monitor suprclass.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/Monitor.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ui.ObjectWatcherComposite;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class Monitor<T extends Identifiable>
		extends ObjectWatcherComposite<T>
{
	private SimplePanel _panel = new SimplePanel();
	
	public Monitor(T object)
    {
	    super(object);
	    initWidget(_panel);
    }
	
	public Widget update()
	{
		try
		{
			return updateImpl(getObject());
		}
		catch (OutOfScopeException e)
		{
			return new Label("(unavailable)");
		}
	}
	
	@Override
	public void onChange(ChangeCallback cb)
	{
		Widget w = update();
		if (_panel.getWidget() != w)
		{
			_panel.clear();
			_panel.add(w);
		}
	}
	
	public abstract String getLabel();
	public abstract Widget updateImpl(T object)
		throws OutOfScopeException;
}
