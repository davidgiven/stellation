package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.game.StarMap;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class StarMapImpl extends Composite implements StarMap,
	RequiresResize
{
	private GWTCanvas _canvas;
	private double _centerx;
	private double _centery;
	
	public StarMapImpl()
    {
		_canvas = new GWTCanvas();
		initWidget(_canvas);
    }
	
	@Override
	public void onResize()
	{
		Widget parent = getParent();
		int w = parent.getOffsetWidth();
		int h = parent.getOffsetHeight();
		_canvas.setPixelSize(w, h);
		_canvas.setCoordSize(w, h);
		redraw();
	}
	
	private void redraw()
	{
		_canvas.setBackgroundColor(Color.BLUE);
		_canvas.clear();
		_canvas.setStrokeStyle(Color.WHITE);
		_canvas.beginPath();
		_canvas.moveTo(0, 0);
		_canvas.lineTo(getOffsetWidth(), getOffsetHeight());
		_canvas.stroke();
	}
}
