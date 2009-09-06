/* A widget containing a canvas.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/canvas/Canvas.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui.canvas;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class Canvas extends Widget implements HasAllMouseHandlers
{
	Glyph _glyph = new RootGlyph();
	
	public Canvas()
	{
		_glyph.setStyle("position", "relative");
		setElement(_glyph.getElement());
    }
	
	public Glyph getRootGlyph()
	{
		return _glyph;
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler)
    {
	    return addDomHandler(handler, MouseOverEvent.getType());
    }

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler)
    {
	    return addDomHandler(handler, MouseDownEvent.getType());
    }

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler)
    {
	    return addDomHandler(handler, MouseUpEvent.getType());
    }

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler)
    {
	    return addDomHandler(handler, MouseOutEvent.getType());
    }

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler)
    {
	    return addDomHandler(handler, MouseWheelEvent.getType());
    }

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler)
    {
	    return addDomHandler(handler, MouseMoveEvent.getType());
    }	
}
