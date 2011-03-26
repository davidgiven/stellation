package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class ResizingCanvas extends Canvas implements RequiresResize,
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
		resizeCanvas(w, h);
		ResizeEvent.fire(this, w, h);
	}
	
	@Override
	protected void onAttach()
	{
	    super.onAttach();
	    onResize();
	}
}
