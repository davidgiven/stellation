/* A custom tab panel.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/TabPanel.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabPanel extends Composite
{
	private class Tab
	{
		Widget content;
		ToggleButton selector;
	};
	
	private VerticalPanel _main = new VerticalPanel();
	private HorizontalPanel _selectors = new HorizontalPanel();
	private DeckPanel _content = new DeckPanel();
	private Vector<Tab> _tabs = new Vector<Tab>();
	
	public TabPanel()
    {
		initWidget(_main);
		setStylePrimaryName("TabbedPanel");

		_main.add(_selectors);
		_main.add(_content);
		_content.setHeight("100%");
		_content.setStylePrimaryName("TabbedPanelInner");
    }
	
	public void add(Widget body, String label)
	{
		final Tab tab = new Tab();
		tab.content = body;
		tab.selector = new ToggleButton(label,
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						selectTab(tab);
					}
				}
		);
		_tabs.add(tab);
		_selectors.add(tab.selector);
		_content.add(tab.content);
	}
	
	public void remove(Widget body)
	{
		for (int i = 0; i < _tabs.size(); i++)
		{
			Tab t = _tabs.get(i);
			if (t.content == body)
			{
				_selectors.remove(t.selector);
				_content.remove(t.content);
				_tabs.remove(i);
				return;
			}
		}
	}

	private void selectTab(Tab tab)
	{
		for (int i = 0; i < _tabs.size(); i++)
		{
			Tab t = _tabs.get(i);
			if (t == tab)
			{
				t.selector.setDown(true);
				_content.showWidget(i);
			}
			else
				t.selector.setDown(false);
		}
	}
	
	public void selectTab(Widget w)
	{
		for (int i = 0; i < _tabs.size(); i++)
		{
			Tab t = _tabs.get(i);
			if (t.content == w)
			{
				t.selector.setDown(true);
				_content.showWidget(i);
			}
			else
				t.selector.setDown(false);
		}
	}
	
	public void selectTab(int index)
	{
		selectTab(_tabs.get(index));
	}
}
