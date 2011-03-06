package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class Screen extends Composite implements RequiresResize, ProvidesResize
{
	private static ScreenUiBinder uiBinder = GWT.create(ScreenUiBinder.class);
	
	interface ScreenUiBinder extends UiBinder<Widget, Screen>
	{
	}
	
	public Screen()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField
	LayoutPanel starmapContainer;
	
	public Screen(String firstName)
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void onResize()
	{
		((RequiresResize) getWidget()).onResize();
	}
	
	public void setStarmap(StarMapImpl sm)
	{
		starmapContainer.clear();
		starmapContainer.add(sm);
		starmapContainer.forceLayout();
	}
}
