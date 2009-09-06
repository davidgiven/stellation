/* Monitors the brightness of a star.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/StarBrightnessMonitor.java,v $
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

public class StarBrightnessMonitor extends Monitor<CStar>
{
	private static final String[] _brightnessTab = new String[]
	{
		"no star present",
		"T (methane dwarf)",
		"L (brown dwarf)",
		"M (red)",
		"K (orange)",
		"G (yellow)",
		"F (white)",
		"A (blue-white)",
		"B (blue)",
		"O (ultraviolet)"
	};
	
	private Label _label = new Label();
	
	public StarBrightnessMonitor(CStar star)
    {
		super(star);
    }
	
	public String getLabel()
	{
		return "Brightness";
	}
	
	public Widget updateImpl(CStar object) throws OutOfScopeException
	{
		double b = object.getBrightness();
		
		StringBuilder sb = new StringBuilder();
		sb.append(S.BRIGHTNESS_FORMAT.format(b));
		sb.append(", ");
		
		int i = (int) b;
		if (i >= _brightnessTab.length)
			i = _brightnessTab.length - 1;
		
		sb.append(_brightnessTab[i]);

		_label.setText(sb.toString());
		return _label;
	}
}
