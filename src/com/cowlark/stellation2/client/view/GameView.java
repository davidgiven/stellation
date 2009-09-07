/* The overall game screen.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/GameView.java,v $
 * $Date: 2009/09/07 22:28:13 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.client.view;

import java.util.HashMap;
import java.util.Map;
import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.client.ui.FullScreenPanel;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class GameView extends Composite implements ClickHandler
{
	public final static int BORDER = 5;
	public final static int TABHEIGHT = 20;
	public final static int CLOCKHEIGHT = 50;
	public final static int LEFTWIDTH = 200;
	public final static int RIGHTWIDTH = 200;

	private FullScreenPanel _panel = new FullScreenPanel();
	private ClockView _clockpane = new ClockView();
	private LeftPaneView _leftpane = new LeftPaneView();
	private HorizontalPanel _tabbar = new HorizontalPanel();
	private Widget _rightpane = new BlankView();
	
	private HelpView _helpview = new HelpView();
	private MapView _mapview = new MapView();
	private LogView _logview = new LogView();
	private MiddlePaneStarView _starview;
	
	private Map<Widget, Widget> _contentForButton = new HashMap<Widget, Widget>();
	private Map<Widget, Widget> _buttonForContent = new HashMap<Widget, Widget>();
	private Widget _selectedTabContent;
	private Widget _selectedTabButton;
	
	public GameView()
    {
		initWidget(_panel);
		
		_tabbar.setStylePrimaryName("TabBar");
		_rightpane.setStylePrimaryName("SimplePanel");
		
		_panel.add(_leftpane, BORDER*2, BORDER*2, LEFTWIDTH-BORDER, -(CLOCKHEIGHT+BORDER*4));
		_panel.add(_clockpane, BORDER*2, -(CLOCKHEIGHT+BORDER*2), LEFTWIDTH-BORDER, -BORDER*2); 
		_panel.add(_tabbar, LEFTWIDTH+BORDER, BORDER*2, -(RIGHTWIDTH+BORDER), TABHEIGHT + BORDER*2);
		showRightPaneView(_rightpane);
		
		addTab("Help", _helpview);
		addTab("Map", _mapview);
		addTab("Intelligence", _logview);
		if (Stellation2.getPlayer().isAdministrator())
			addTab("Admin", new AdminView());
		
		selectTab(_helpview);
    }
	
	private void addMiddlePane(Widget w)
	{
		_panel.add(w, LEFTWIDTH+BORDER, TABHEIGHT + BORDER*2, -(RIGHTWIDTH+BORDER), -BORDER*2);
	}
	
	private Widget createTabButton(String label)
	{
		Label w = new Label(label);
		w.setSize("100%", "100%");
		w.setStylePrimaryName("TabButton");
		w.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		w.addClickHandler(this);
		return w;
	}

	public void onClick(ClickEvent event)
	{
		Object source = event.getSource();
		if (!(source instanceof Widget))
			return;
		
		Widget content = _contentForButton.get((Widget) source);
		if (content != null)
			selectTab(content);
	}
	
	public void addTab(String label, Widget content)
	{
		Widget button = createTabButton(label);
		
		_contentForButton.put(button, content);
		_buttonForContent.put(content, button);

		content.setVisible(false);
		_tabbar.add(button);
		addMiddlePane(content);
		selectTab(content);
	}
	
	public void removeTab(Widget content)
	{
		Widget button = _buttonForContent.get(content);
		if (button != null)
			_tabbar.remove(button);
		if (content != null)
			_panel.remove(content);
		
		_buttonForContent.remove(content);
		_contentForButton.remove(button);
	}
	
	public void selectTab(Widget content)
	{
		if (_selectedTabButton != null)
			_selectedTabButton.removeStyleName("SelectedTabButton");
		if (_selectedTabContent != null)
			_selectedTabContent.setVisible(false);
		
		_selectedTabButton = _buttonForContent.get(content);
		_selectedTabContent = content;
		
		if (_selectedTabButton != null)
			_selectedTabButton.addStyleName("SelectedTabButton");
		if (_selectedTabContent != null)
			_selectedTabContent.setVisible(true);
	}
	
	public void showStarViewer(CStar star)
	{
		removeTab(_starview);
	
		_starview = new MiddlePaneStarView(star);
		addTab(star.getName(), _starview);
	}
	
	public void showRightPaneView(Widget pane)
	{
		if (_rightpane != null)
			_panel.remove(_rightpane);

		_rightpane = pane;
		_panel.add(_rightpane, -(RIGHTWIDTH-BORDER), BORDER*2, -(BORDER*2), -BORDER*2);
	}	
}
