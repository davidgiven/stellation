package com.cowlark.stellation3.gwt.ui.drawingarea;

import com.google.gwt.dom.client.Element;

public class Group extends Shape implements HasShapes
{
	public Group(Element e)
	{
		super(e);
	}	
	
	@Override
	public void addShape(Shape shape)
	{
		getElement().appendChild(shape.getElement());
	}
}
