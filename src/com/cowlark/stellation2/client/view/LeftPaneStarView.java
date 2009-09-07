/* Shows a star in the left plane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/LeftPaneStarView.java,v $
 * $Date: 2009/09/07 21:48:10 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import java.util.HashSet;
import java.util.Set;
import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.monitors.FleetNameMonitor;
import com.cowlark.stellation2.client.monitors.StarNameMonitor;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.SyncableObjectList;
import com.cowlark.stellation2.common.SyncableObjectManager;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CFleet;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LeftPaneStarView extends AbstractView<CStar>
		implements SyncableObjectManager<FleetNameMonitor>
{
	private FlowPanel _fleetPanel = new FlowPanel();
	private SyncableObjectList<FleetNameMonitor> _fleets =
		new SyncableObjectList<FleetNameMonitor>();
	
	public LeftPaneStarView(CStar star)
    {
		super(star);

		VerticalPanel panel = new VerticalPanel();
		initWidget(panel);

		panel.add(new StarNameMonitor(star));
		
		_fleetPanel.addStyleName("LeftPaneFleetPanel");
		panel.add(_fleetPanel);
    }

	@Override
	public void onChange(ChangeCallback cb)
	{
		Set<Identifiable> set = new HashSet<Identifiable>();

		try
		{
			CStar star = getObject();
			
			for (CObject o : star)
			{
				CFleet fleet = o.toFleet();
				if (fleet != null)
					set.add(fleet);
			}
		}
		catch (OutOfScopeException e)
		{
		}
		
		_fleets.syncWith(set, this);
	}
	
	public FleetNameMonitor create(long id)
	{
		CFleet fleet = (CFleet) ClientDB.get(id);
		FleetNameMonitor fnm = new FleetNameMonitor(fleet);
	    _fleetPanel.add(fnm);
	    return fnm;
	}
	
	public void destroy(FleetNameMonitor object)
    {
		object.removeFromParent();
    }
}
