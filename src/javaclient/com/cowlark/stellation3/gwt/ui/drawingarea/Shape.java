package com.cowlark.stellation3.gwt.ui.drawingarea;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class Shape extends UIObject
{
	private double _dx = 0.0;
	private double _dy = 0.0;
	private double _scale = 1.0;
	private double _rotation = 0.0;
	
	Shape(Element element)
    {
		setElement(element);
    }
	
	public void setClass(String classname)
	{
		DrawingArea.setAttributeNS(getElement(), "class", classname);
	}
	
	public void setOpacity(double opacity)
	{
		getElement().setAttribute("opacity", String.valueOf(opacity));
	}
	
	public void setShapeRendering(String mode)
	{
		getElement().setAttribute("shape-rendering", mode);
	}
	
	public void setStroke(String stroke)
	{
		getElement().setAttribute("stroke", stroke);
	}
	
	public void setStrokeWidth(String strokewidth)
	{
		getElement().setAttribute("stroke-width", strokewidth);
	}
	
	public void setFill(String fill)
	{
		getElement().setAttribute("fill", fill);
	}
	
	private void updateTransformation()
	{
		StringBuilder sb = new StringBuilder();
		
		if ((_dx != 0.0) || (_dy != 0.0))
		{
			sb.append("translate(");
			sb.append(_dx);
			sb.append(",");
			sb.append(_dy);
			sb.append(") ");
		}
		
		if (_scale != 1.0)
		{
			sb.append("scale(");
			sb.append(_scale);
			sb.append(") ");
		}
		
		if (_rotation != 0.0)
		{
			sb.append("rotate(");
			sb.append(_rotation);
			sb.append(") ");
		}
		
		if (sb.length() > 0)
			getElement().setAttribute("transform", sb.toString());
	}
	
	public void setTranslation(double dx, double dy)
	{
		_dx = dx;
		_dy = dy;
		updateTransformation();
	}
	
	public void setScale(double scale)
	{
		_scale = scale;
		updateTransformation();
	}
	
	public void setRotation(double theta)
	{
		_rotation = theta;
		updateTransformation();
	}
}
