/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/FactoryCompletionMonitor.java,v $
 * $Date: 2009/09/20 21:50:35 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.Utils;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CFactory;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FactoryCompletionMonitor extends Monitor<CFactory>
{
	public Label _label = new Label();

	public FactoryCompletionMonitor(CFactory unit)
    {
		super(unit);
    }
	
	public String getLabel()
	{
		return "Completion time";
	}
	
	public Widget updateImpl(CFactory factory) throws OutOfScopeException
	{
		long time = factory.getCompletionTime();
		if (time == -1)
			_label.setText("n/a");
		else
			_label.setText(Utils.renderTime(time));

		return _label;
	}
}
