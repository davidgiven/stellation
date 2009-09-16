/* Monitors the name of a star.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/TugCargoMonitor.java,v $
 * $Date: 2009/09/16 23:14:51 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import java.util.Iterator;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.common.model.CTug;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TugCargoMonitor extends Monitor<CTug>
{
	public Label _label = new Label();

	public TugCargoMonitor(CTug tug)
    {
		super(tug);
    }
	
	public String getLabel()
	{
		return "Towing";
	}
	
	public Widget updateImpl(CTug tug)
			throws OutOfScopeException
	{
		Iterator<CObject> i = tug.iterator();
		if (i.hasNext())
		{
			CObject o = i.next();
			_label.setText(o.getProperties().getName());
		}
		else
			_label.setText("nothing");
		
		return _label;
	}
}
