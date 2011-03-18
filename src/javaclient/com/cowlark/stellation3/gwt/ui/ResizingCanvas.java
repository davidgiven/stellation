package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class ResizingCanvas extends Widget implements RequiresResize,
	HasResizeHandlers
{
	private CanvasElement _canvas;
	
	public ResizingCanvas()
    {
		_canvas = Document.get().createCanvasElement();
		setElement(_canvas);
    }
	
	public Context2d getContext2d()
	{
		return _canvas.getContext2d();
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
		_canvas.setWidth(w);
		_canvas.setHeight(h);
		ResizeEvent.fire(this, w, h);
	}
	
	public <H extends EventHandler> HandlerRegistration
		addHandler(H handler, DomEvent.Type<H> type)
	{
		return addDomHandler(handler, type);
	}
}
