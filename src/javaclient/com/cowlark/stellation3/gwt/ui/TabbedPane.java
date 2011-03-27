package com.cowlark.stellation3.gwt.ui;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.gwt.ControllerRenderer;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabbedPane implements Pane, Comparable<TabbedPane>
{
	private PaneAspect _aspect;
	private PaneHandler _ph;
	private ScrollPanel _body;
	private String _title;
	private ControllerRenderer _renderer;
	private TabbedPaneContainer _container;
	
	public TabbedPane(PaneAspect aspect, PaneHandler ph, String title)
    {
		_aspect = aspect;
		_ph = ph;
		_body = new ScrollPanel();
		_title = title;
		_renderer = new ControllerRenderer();
		_body.add(_renderer);
    }
	
	@Override
	public int compareTo(TabbedPane o)
	{
		return _aspect.compareTo(o._aspect);
	}
	
	public String getTab()
	{
		return _title;
	}
	
	public Widget getBody()
	{
		return _body;
	}
	
	public void attach(TabbedPaneContainer container)
	{
		_container = container;
	}
	
	@Override
	public void updateControllers(List<Controller> controllers)
	{
		_renderer.update(controllers);
	}
	
	@Override
	public void cancelPane()
	{
		if (_container != null)
			_container.remove(this);
	}
	
	@Override
	public void closePane()
	{
		if (_container != null)
			_container.remove(this);
	}
}
