/* A generic thing that can be drawn on a canvas.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/canvas/Glyph.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui.canvas;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public abstract class Glyph
{
	private final Element _element;
	
	public Glyph(Element element)
	{
		_element = element;
		element.setPropertyString("className", "Glyph");
	}
	
	public final Element getElement()
	{
		return _element;
	}
	
	public final Glyph setStyle(String name, String value)
	{
		DOM.setStyleAttribute(_element, name, value);
		return this;
	}
	
	public final Glyph addClass(String style)
	{
		String c = _element.getPropertyString("className");
		_element.setPropertyString("className", c + " " + style);
		return this;
	}
	
	public final Glyph setPos(int x, int y)
	{
		setStyle("position", "absolute");
		setStyle("left", x + "px");
		setStyle("top", y + "px");
		return this;
	}
	
	public final Glyph setSize(int w, int h)
	{
		setStyle("width", w + "px");
		setStyle("height", h + "px");
		return this;
	}
	
	public final Glyph clear()
	{
		_element.setInnerHTML("");
		return this;
	}
	
	public final void add(int x, int y, Glyph glyph)
	{
		glyph.setPos(x, y);
		_element.appendChild(glyph.getElement());
	}
}
