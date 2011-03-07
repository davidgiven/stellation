package com.cowlark.stellation3.gwt.ui.drawingarea;

import com.google.gwt.dom.client.Element;

public class Path extends Shape
{
	private StringBuilder _sb;
	
	public Path(Element e)
	{
		super(e);
	}
	
	public void begin()
	{
		_sb = new StringBuilder();
	}
	
	public void end()
	{
		getElement().setAttribute("d", _sb.toString());
	}
	
	private void command(char c, double x, double y)
	{
		_sb.append(c);
		_sb.append(x);
		_sb.append(',');
		_sb.append(y);
	}
		
	public void moveTo(double x, double y)
	{
		command('M', x, y);
	}
	
	public void moveRelativeTo(double x, double y)
	{
		command('m', x, y);
	}
	
	public void lineTo(double x, double y)
	{
		command('L', x, y);
	}
	
	public void lineRelativeTo(double x, double y)
	{
		command('l', x, y);
	}
}
