package com.cowlark.stellation3.gwt.ui.drawingarea;

import java.util.LinkedList;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class DrawingArea extends Widget implements HasShapes
{
	private static final String SVG_NS = "http://www.w3.org/2000/svg";
	
	static native Element createElementNS(String ns, String tag) /*-{
	    return $doc.createElementNS(ns, tag);
	}-*/;
	
	static void setAttributeNS(Element elem, String attr, int value) {
	    setAttributeNS(null, elem, attr, "" + value);
	}
	
	static void setAttributeNS(Element elem, String attr, String value) {
	    setAttributeNS(null, elem, attr, value);
	}
	
	static native void setAttributeNS(String uri, Element elem,
	            String attr, String value) /*-{
	    elem.setAttributeNS(uri, attr, value);
	}-*/;

	private Element _element;
	
	public DrawingArea()
    {
		_element = createElementNS(SVG_NS, "svg");
		_element.setAttribute("version", "1.1");
		setElement(_element);
    }
	
	@Override
	public void setPixelSize(int width, int height)
	{
	    super.setPixelSize(width, height);
	    setAttributeNS(_element, "width", String.valueOf(width));
	    setAttributeNS(_element, "height", String.valueOf(height));
	}
	
	public void addShape(Shape child)
	{
		_element.appendChild(child.getElement());
	}
	
	public Group createGroup()
	{
		Element e = createElementNS(SVG_NS, "g");
		return new Group(e);
	}
	
	public Line createLine(double x1, double y1, double x2, double y2)
	{
		Element e = createElementNS(SVG_NS, "line");
		e.setAttribute("x1", String.valueOf(x1));
		e.setAttribute("y1", String.valueOf(y1));
		e.setAttribute("x2", String.valueOf(x2));
		e.setAttribute("y2", String.valueOf(y2));
		return new Line(e);
	}
	
	public Circle createCircle(double cx, double cy, double r)
	{
		Element e = createElementNS(SVG_NS, "circle");
		e.setAttribute("cx", String.valueOf(cx));
		e.setAttribute("cy", String.valueOf(cy));
		e.setAttribute("r", String.valueOf(r));
		return new Circle(e);
	}
	
	public Path createPath()
	{
		Element e = createElementNS(SVG_NS, "path");
		return new Path(e);
	}
}
