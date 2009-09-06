/* Monitors the position of a star.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/StarPositionMonitor.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StarPositionMonitor extends Monitor<CStar>
{
	private Label _label = new Label();

	public StarPositionMonitor(CStar star)
    {
		super(star);
    }
	
	public String getLabel()
	{
		return "Position";
	}
	
	public Widget updateImpl(CStar object) throws OutOfScopeException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(S.COORD_FORMAT.format(object.getX()));
		sb.append(", ");
		sb.append(S.COORD_FORMAT.format(object.getY()));
		sb.append(")");

		_label.setText(sb.toString());
		return _label;
	}
}
