/* Monitors resources in any HasResources object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/ResourcesMonitor.java,v $
 * $Date: 2009/09/08 23:01:28 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.HasResources;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ResourcesMonitor extends Monitor<HasResources>
{
	private Label _label = new Label();
	
	public ResourcesMonitor(HasResources o)
    {
		super(o);
    }
	
	public String getLabel()
	{
		return "Resources";
	}
	
	public Widget updateImpl(HasResources hr) throws OutOfScopeException
	{
		Resources r = hr.getResources();

		StringBuilder sb = new StringBuilder();
		sb.append("A: ");
		sb.append(r.getAntimatter());
		sb.append(" M: ");
		sb.append(r.getMetal());
		sb.append(" O: ");
		sb.append(r.getOrganics());

		_label.setText(sb.toString());
		return _label;
	}
}
