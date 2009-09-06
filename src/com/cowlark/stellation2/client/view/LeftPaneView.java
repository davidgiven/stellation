/* Handles the overall left pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/LeftPaneView.java,v $
 * $Date: 2009/09/06 22:17:53 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import java.util.HashSet;
import java.util.Set;
import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.client.ui.Trigger;
import com.cowlark.stellation2.client.ui.WrappedLabel;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.SyncableObjectList;
import com.cowlark.stellation2.common.SyncableObjectManager;
import com.cowlark.stellation2.common.model.CPlayer;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LeftPaneView extends Composite implements ChangeCallback
{
	private ScrollPanel _scrollpane = new ScrollPanel();
	private FlowPanel _panel = new FlowPanel();
	private SyncableObjectList<LeftPaneStarView> _stars;
	private Trigger _updateTrigger;
	private VerticalPanel _starpanel;
	
	public LeftPaneView()
    {
		initWidget(_scrollpane);
		_scrollpane.setAlwaysShowScrollBars(false);
		_scrollpane.add(_panel);
		
		setStylePrimaryName("SimplePanel");
		_panel.setStylePrimaryName("PanelInner");
		
		_stars = new SyncableObjectList<LeftPaneStarView>();
		_starpanel = new VerticalPanel();
		_updateTrigger = new Trigger("Update",
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						_updateTrigger.setEnabled(false);
						ClientDB.fetchUpdates();
					};
				}
		);
			
		ClientDB.addChangeCallback(Stellation2.getPlayer(), this);
		onChange(this);
    }
	
	public void close()
	{
	}

	public void onChange(ChangeCallback cb)
    {
		CPlayer player = Stellation2.getPlayer();
		
		_panel.clear();
		_panel.add(new WrappedLabel(player.getName()));
		_panel.add(new WrappedLabel("of"));
		_panel.add(new WrappedLabel(player.getEmpire()));
		
		_updateTrigger.setEnabled(true);
		_panel.add(_updateTrigger);
		
		_panel.add(_starpanel);
		
		Set<CStar> visibleStars = player.getVisibleStarsystems();
		Set<Identifiable> set = new HashSet<Identifiable>();
		set.addAll(visibleStars);
		
		_stars.syncWith(set,
				new SyncableObjectManager<LeftPaneStarView>()
				{
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
		);
    }
}
