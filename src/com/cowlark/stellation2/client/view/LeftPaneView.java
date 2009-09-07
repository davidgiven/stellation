/* Handles the overall left pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/LeftPaneView.java,v $
 * $Date: 2009/09/07 21:48:10 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.client.view;

import java.util.HashSet;
import java.util.Set;
import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.client.monitors.PlayerTitleMonitor;
import com.cowlark.stellation2.client.ui.NonWrappedLabel;
import com.cowlark.stellation2.client.ui.ObjectWatcherComposite;
import com.cowlark.stellation2.client.ui.Trigger;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.SyncableObjectList;
import com.cowlark.stellation2.common.SyncableObjectManager;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CPlayer;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class LeftPaneView extends ObjectWatcherComposite<CPlayer>
		implements SyncableObjectManager<LeftPaneStarView>
{
	private ScrollPanel _scrollpane = new ScrollPanel();
	private FlowPanel _panel = new FlowPanel();
	private SyncableObjectList<LeftPaneStarView> _stars =
			new SyncableObjectList<LeftPaneStarView>();
	private FlowPanel _starpanel = new FlowPanel();
	
	private Trigger _updateTrigger = new Trigger("Update",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
					_updateTrigger.setEnabled(false);
					ClientDB.fetchUpdates();
				};
			}
		);
	
	private Trigger _logoutTrigger = new Trigger("Logout",
			new ClickHandler()
	{
		public void onClick(ClickEvent event)
		{
			Stellation2.logout();
		};
	}
);
	
	public LeftPaneView()
    {
		super(Stellation2.getPlayer());
		
		initWidget(_scrollpane);
		_scrollpane.setAlwaysShowScrollBars(false);
		_scrollpane.add(_panel);
		
		setStylePrimaryName("SimplePanel");
		_panel.setStylePrimaryName("PanelInner");
		
		CPlayer player = Stellation2.getPlayer();
		_panel.add(new PlayerTitleMonitor(player));
		
		_updateTrigger.setWidth("100%");
		_logoutTrigger.setWidth("100%");
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setWidth("100%");
		hpanel.add(_updateTrigger);
		hpanel.add(_logoutTrigger);
		_panel.add(hpanel);
		
		NonWrappedLabel label = new NonWrappedLabel("Areas of influence:");
		label.addStyleName("LeftPaneSeparator");
		_panel.add(label);
		
		_starpanel.addStyleName("LeftPaneStarPanel");
		_panel.add(_starpanel);
    }
	
	public void onChange(ChangeCallback cb)
    {
		_updateTrigger.setEnabled(true);
		
		Set<Identifiable> set = new HashSet<Identifiable>();

		try
		{
			CPlayer player = getObject();
			
			Set<CStar> visibleStars = player.getVisibleStarsystems();
			set.addAll(visibleStars);
			
			_stars.syncWith(set, this);
		}
		catch (OutOfScopeException e)
		{
			_stars.syncWith(set, this);
		}
    }
	
	public LeftPaneStarView create(long id)
	{
		CStar star = (CStar) ClientDB.get(id);
	    LeftPaneStarView sv = new LeftPaneStarView(star);
	    _starpanel.add(sv);
	    return sv;
	}
	
	public void destroy(LeftPaneStarView object)
    {
		object.removeFromParent();
    }
}
