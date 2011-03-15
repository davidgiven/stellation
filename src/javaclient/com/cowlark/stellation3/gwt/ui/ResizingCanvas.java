package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class ResizingCanvas extends GWTCanvas implements RequiresResize,
	HasResizeHandlers
{
	public ResizingCanvas()
    {
    }
	
	@Override
	public HandlerRegistration addResizeHandler(ResizeHandler handler)
	{
		return addHandler(handler, ResizeEvent.getType());
	}
	
	@Override
	public void onResize()
	{
		Widget parent = getParent();
		int w = parent.getOffsetWidth();
		int h = parent.getOffsetHeight();
		resize(w, h);
		ResizeEvent.fire(this, w, h);
	}
	
	public <H extends EventHandler> HandlerRegistration
		addHandler(H handler, DomEvent.Type<H> type)
	{
		return addDomHandler(handler, type);
	}
}
