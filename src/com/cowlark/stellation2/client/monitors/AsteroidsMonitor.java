/* Monitors the number of asteroids in a star.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/AsteroidsMonitor.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.common.Asteroids;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AsteroidsMonitor extends Monitor<CStar>
{
	public Label _label = new Label();
	
	public AsteroidsMonitor(CStar star)
    {
		super(star);
    }
	
	public String getLabel()
	{
		return "Asteroids";
	}
	
	public Widget updateImpl(CStar object) throws OutOfScopeException
	{
		Asteroids a = object.getAsteroids();
		
		StringBuilder sb = new StringBuilder();
		sb.append("M: ");
		sb.append(a.getMetallic());
		sb.append(" C: ");
		sb.append(a.getCarboniferous());
		
		_label.setText(sb.toString());
		return _label;
	}
}
