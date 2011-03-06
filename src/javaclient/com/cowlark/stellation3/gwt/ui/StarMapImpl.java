package com.cowlark.stellation3.gwt.ui;

import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.game.Game;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.CanvasGradient;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class StarMapImpl extends Composite implements 
	Pane, RequiresResize, MouseWheelHandler
{
	private ControllerGroup _cg;
	private PaneHandler _ph;
	private Vector<StarMapStarControllerImpl> _starImpls;
	private GWTCanvas _canvas;
	private double _galactic_radius;
	private double _scale;
	private double _centerx;
	private double _centery;
	
	public StarMapImpl(ControllerGroup cg, PaneHandler ph)
    {
		_cg = cg;
		_ph = ph;
		_canvas = new GWTCanvas();
		initWidget(_canvas);
		
		addDomHandler(this, MouseWheelEvent.getType());
		
		_starImpls = new Vector<StarMapStarControllerImpl>();
		for (Controller c : cg)
			_starImpls.add((StarMapStarControllerImpl) c);
		
		_galactic_radius = Game.Instance.Static.getDouble(Hash.SGalaxy, Hash.Radius);
		
		_scale = 10.0;
		_centerx = 0.0;
		_centery = 0.0;
		
		redraw();
    }
	
	@Override
	public void onResize()
	{
		Widget parent = getParent();
		int w = parent.getOffsetWidth();
		int h = parent.getOffsetHeight();
		_canvas.resize(w, h);
		redraw();
	}
	
	private void redraw()
	{
		double mw = _canvas.getCoordWidth() / 2;
		double mh = _canvas.getCoordHeight() / 2;
		
		double pixel = 1.0/_scale;
		
		_canvas.setBackgroundColor(Color.BLACK);
		_canvas.clear();
		_canvas.saveContext();
		_canvas.translate(mw, mh);
		_canvas.scale(_scale, _scale);
		_canvas.setLineWidth(1.0*pixel);
		
		Color none = new Color("none");
		_canvas.setFillStyle(none);
		_canvas.setStrokeStyle(Color.BLUE);
		double r = _galactic_radius;
		double r2 = r * 2.0;
		for (double d = 0; d < _galactic_radius; d += 1.0)
		{
			_canvas.beginPath();
			_canvas.moveTo(-d, -r);
			_canvas.lineTo(-d, +r);
			_canvas.stroke();
			
			_canvas.beginPath();
			_canvas.moveTo(+d, -r);
			_canvas.lineTo(+d, +r);
			_canvas.stroke();
			
			_canvas.beginPath();
			_canvas.moveTo(-r, -d);
			_canvas.lineTo(+r, -d);
			_canvas.stroke();
			
			_canvas.beginPath();
			_canvas.moveTo(-r, +d);
			_canvas.lineTo(+r, +d);
			_canvas.stroke();
		}
		
		_canvas.setStrokeStyle(Color.WHITE);
		double p5 = 5.0 * pixel;
		for (StarMapStarControllerImpl impl : _starImpls)
		{
			StarMapStarController.StarData sd = impl.getStarData();
			
			double x = sd.x;
			double y = sd.y;
			
			_canvas.beginPath();
			_canvas.moveTo(x-p5, y);
			_canvas.lineTo(x+p5, y);
			_canvas.moveTo(x, y-p5);
			_canvas.lineTo(x, y+p5);
			_canvas.stroke();
		}
		
		_canvas.restoreContext();
	}
	
	@Override
	public void onMouseWheel(MouseWheelEvent event)
	{
		if (event.isNorth())
			_scale *= 1.1;
		else
			_scale /= 1.1;
		redraw();
	}
	
	@Override
	public void cancelPane()
	{
	}
	
	@Override
	public void closePane()
	{
	}
}
