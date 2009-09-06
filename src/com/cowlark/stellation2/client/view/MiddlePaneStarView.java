/* The Star pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/MiddlePaneStarView.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import java.util.HashSet;
import java.util.Set;
import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.monitors.AsteroidsMonitor;
import com.cowlark.stellation2.client.monitors.ResourcesMonitor;
import com.cowlark.stellation2.client.monitors.StarBrightnessMonitor;
import com.cowlark.stellation2.client.monitors.StarNameMonitor;
import com.cowlark.stellation2.client.monitors.StarPositionMonitor;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.ui.DataTable;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.SyncableObjectList;
import com.cowlark.stellation2.common.SyncableObjectManager;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CFleet;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class MiddlePaneStarView extends AbstractView<CStar>
		implements SyncableObjectManager<AbstractGroup<?>>
{
	private ScrollPanel _scrollpanel = new ScrollPanel();
	private FlowPanel _panel = new FlowPanel();
	private DataTable _contentGrid = new DataTable();
	private DataGroup _noContentGroup = new DataGroup().
			setHeader("You cannot see into this star system unless you have " +
					"a jumpship present.");
	private SyncableObjectList<AbstractGroup<?>> _contents =
		new SyncableObjectList<AbstractGroup<?>>();
	
	private static StarImages _starImages =
		(StarImages) GWT.create(StarImages.class);
	
	public MiddlePaneStarView(CStar star)
    {
		super(star);		
		initWidget(_scrollpanel);
		setStylePrimaryName("SimplePanel");
		
		_scrollpanel.add(_panel);
		_scrollpanel.setAlwaysShowScrollBars(false);
		_panel.setStylePrimaryName("PanelInner");

		DataTable table = new DataTable();
		table.addGroup().
				add(new TabularMonitorRow(new StarNameMonitor(star))).
				add(new TabularMonitorRow(new StarPositionMonitor(star))).
				add(new TabularMonitorRow(new StarBrightnessMonitor(star))).
				add(new TabularMonitorRow(new AsteroidsMonitor(star))).
				add(new TabularMonitorRow(new ResourcesMonitor(star)));
		
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.add(_starImages.star().createImage());
		hpanel.add(table);

		_panel.add(hpanel);
		_panel.add(_contentGrid);
    }
	
	@Override
	public void onChange(ChangeCallback cb)
	{
		Set<Identifiable> set = new HashSet<Identifiable>();
		
		try
		{
			CStar star = getObject();		
			set.addAll(star.getContents());
			
			if (star.getScope() == S.GLOBAL)
				_contentGrid.addGroup(_noContentGroup);
			else
				_contentGrid.removeGroup(_noContentGroup);
		}
		catch (OutOfScopeException e)
		{
			_contentGrid.addGroup(_noContentGroup);
		}

		_contents.syncWith(set, this);
	}
	
	
	public AbstractGroup<?> create(long id)
	{
		CObject obj = Database.get(id);
		CFleet fleet = obj.toFleet();
		if (fleet != null)
		{
			MiddlePaneFleetGroup group = new MiddlePaneFleetGroup(fleet);
			_contentGrid.addGroup(group);
			return group;
		}
		return null;
	}
	
	public void destroy(AbstractGroup<?> object)
    {
		_contentGrid.removeGroup(object);
    }
}
