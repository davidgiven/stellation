package com.cowlark.stellation3.gwt.ui;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.gwt.ControllerRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class StaticPane extends Composite implements Pane
{
	private static StaticPaneUiBinder uiBinder = GWT
	        .create(StaticPaneUiBinder.class);
	
	interface StaticPaneUiBinder extends UiBinder<Widget, StaticPane>
	{
	}
	
	private ControllerRenderer _renderer;
	private PaneHandler _ph;
	
	@UiField
	ScrollPanel Container;
	
	public StaticPane(PaneHandler ph)
	{
		_renderer = new ControllerRenderer();
		_ph = ph;
		initWidget(uiBinder.createAndBindUi(this));
		
		Container.add(_renderer.getContainer());
	}
	
	@Override
	public void updateControllers(List<Controller> controllers)
	{
		_renderer.update(controllers);
	}
	
	@Override
	public void cancelPane()
	{
	}
	
	@Override
	public void closePane()
	{
	}
}
