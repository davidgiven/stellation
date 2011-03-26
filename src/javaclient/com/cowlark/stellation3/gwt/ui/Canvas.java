package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class Canvas extends Widget
{
	private CanvasElement _canvas;
	
	public Canvas()
    {
		_canvas = Document.get().createCanvasElement();
		setElement(_canvas);
    }
	
	public Context2d getContext2d()
	{
		return _canvas.getContext2d();
	}
	
	public CanvasElement getCanvasElement()
	{
		return _canvas;
	}
	
	public void resizeCanvas(int w, int h)
	{
		_canvas.setWidth(w);
		_canvas.setHeight(h);
	}
	
	public String toDataUrl()
	{
		return _canvas.toDataUrl();
	}
	
	public String toDataUrl(String type)
	{
		return _canvas.toDataUrl(type);
	}
	
	public <H extends EventHandler> HandlerRegistration
		addHandler(H handler, DomEvent.Type<H> type)
	{
		return addDomHandler(handler, type);
	}
}
