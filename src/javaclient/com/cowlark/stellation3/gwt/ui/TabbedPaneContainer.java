package com.cowlark.stellation3.gwt.ui;

import java.util.PriorityQueue;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class TabbedPaneContainer extends TabLayoutPanel
		implements BeforeSelectionHandler<Integer>
{
	private Image _toggleImage;
	private PriorityQueue<TabbedPane> _tabs;
	private boolean _minimised;
	
	public TabbedPaneContainer()
    {
		super(2, Unit.EM);
		
		_toggleImage = new Image(UIResources.Instance.minimise());
		_tabs = new PriorityQueue<TabbedPane>();
		
		add(new Label(), _toggleImage);
		getTabWidget(0).getParent().addStyleName("togglebutton");
		
		addBeforeSelectionHandler(this);
    }
	
	public void add(TabbedPane pane)
	{
		_tabs.add(pane);
		
		int i = 1;
		for (TabbedPane p : _tabs)
		{
			if (p == pane)
			{
				insert(pane.getBody(), pane.getTab(), i);
				selectTab(i);
				break;
			}
			i++;
		}
		
		pane.attach(this);
	}
	
	public void remove(TabbedPane pane)
	{
		_tabs.remove(pane);
		remove(pane.getBody());
	}
	
	@Override
	public void onBeforeSelection(BeforeSelectionEvent<Integer> event)
	{
		int tab = event.getItem();
		if (tab == 0)
		{
			if (_minimised)
			{
				setHeight("100%");
				_minimised = false;
				_toggleImage.setResource(UIResources.Instance.minimise());
			}
			else
			{
				setHeight("2em");
				_minimised = true;
				_toggleImage.setResource(UIResources.Instance.maximise());
			}
			event.cancel();
		}
	}
}
