package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
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
		
		Element root = getElement();
		NodeList<Element> divs = root.getElementsByTagName("div");
		for (int i = 0; i < divs.getLength(); i++)
		{
			Element e = divs.getItem(i);
			
			if (e.getClassName().contains("noevents"))
			{
				while (e != root)
				{
					e.getStyle().setProperty("pointerEvents", "none");
					e = e.getParentElement();
				}
			}				
		}
	}
	
	@UiField
	public ResizingCanvas BackgroundCanvas;
	
	@UiField
	public Label BackgroundBottomLeftLabel;
	
	@UiField
	public LayoutPanel LeftContainer;
	
	public Screen(String firstName)
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void onResize()
	{
		((RequiresResize) getWidget()).onResize();
	}
}
