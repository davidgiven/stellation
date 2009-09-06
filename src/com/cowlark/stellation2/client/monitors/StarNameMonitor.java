/* Monitors the name of a star.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/monitors/StarNameMonitor.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.monitors;

import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.client.ui.Spacer;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StarNameMonitor extends Monitor<CStar>
{
	public HorizontalPanel _panel = new HorizontalPanel();
	public Label _starLabel = new Label();
	public Label _mapLabel = new Label("[map]");

	public StarNameMonitor(CStar star)
    {
		super(star);

		_panel.add(_starLabel);
		_panel.add(new Spacer());
		_panel.add(_mapLabel);
		
		_starLabel.setStyleName("Link");
		_mapLabel.setStyleName("Link");
		
		_starLabel.addClickHandler(
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						try
						{
							Stellation2.showStarViewer(getObject());
						}
						catch (OutOfScopeException e)
						{
						}
					}
				}
		);

		_mapLabel.addClickHandler(
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
					}
				}
		);
    }
	
	public String getLabel()
	{
		return "Name";
	}
	
	public Widget updateImpl(CStar star) throws OutOfScopeException
	{
		_starLabel.setText(star.getName());
		return _panel;
	}
}
