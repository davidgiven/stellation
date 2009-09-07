/* Monitors the name of a unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/PlayerTitleMonitor.java,v $
 * $Date: 2009/09/07 21:48:10 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.client.ui.NonWrappedLabel;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CPlayer;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PlayerTitleMonitor extends Monitor<CPlayer>
{
	public VerticalPanel _panel = new VerticalPanel();
	public NonWrappedLabel _empire = new NonWrappedLabel();
	public NonWrappedLabel _of = new NonWrappedLabel("of");
	public NonWrappedLabel _name = new NonWrappedLabel();

	public PlayerTitleMonitor(CPlayer unit)
    {
		super(unit);
		
		_panel.setStylePrimaryName("TitlePanel");
		_empire.addStyleName("TitleEmpire");
		_of.addStyleName("TitleOf");
		_name.addStyleName("TitleName");
		
		_panel.add(_empire);
		_panel.add(_of);
		_panel.add(_name);
    }
	
	public String getLabel()
	{
		return "Name";
	}
	
	public Widget updateImpl(CPlayer player) throws OutOfScopeException
	{
		_empire.setText(player.getEmpire());
		_name.setText(player.getName());
		return _panel;
	}
}
