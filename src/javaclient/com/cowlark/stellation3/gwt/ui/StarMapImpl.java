package com.cowlark.stellation3.gwt.ui;

import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.gwt.ui.drawingarea.Circle;
import com.cowlark.stellation3.gwt.ui.drawingarea.DrawingArea;
import com.cowlark.stellation3.gwt.ui.drawingarea.Group;
import com.cowlark.stellation3.gwt.ui.drawingarea.HasShapes;
import com.cowlark.stellation3.gwt.ui.drawingarea.Image;
import com.cowlark.stellation3.gwt.ui.drawingarea.Path;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class StarMapImpl extends Composite implements 
	Pane, RequiresResize, MouseWheelHandler, MouseDownHandler,
	MouseUpHandler, MouseMoveHandler, MouseOutHandler
{
	private ControllerGroup _cg;
	private PaneHandler _ph;
	private Vector<StarMapStarControllerImpl> _starImpls;
	private DrawingArea _da;
	private Group _map;
	private Group _majorGraticuleLayer;
	private Group _minorGraticuleLayer;
	private Group _starIconLayer;
	private Group _starNameLayer;
	private double _galactic_radius;
	private double _scale;
	private double _centerx;
	private double _centery;
	private double _dragstartx;
	private double _dragstarty;
	private double _dragdeltax;
	private double _dragdeltay;
	private HandlerRegistration _draghandler;
	
	public StarMapImpl(ControllerGroup cg, PaneHandler ph)
    {
		_cg = cg;
		_ph = ph;
		_da = new DrawingArea();
		initWidget(_da);
		_da.setPixelSize(500, 500);
		
		_map = _da.createGroup();
		_da.addShape(_map);
		addDomHandler(this, MouseWheelEvent.getType());
		addDomHandler(this, MouseDownEvent.getType());
		addDomHandler(this, MouseUpEvent.getType());
		addDomHandler(this, MouseOutEvent.getType());
		
		_starImpls = new Vector<StarMapStarControllerImpl>();
		for (Controller c : cg)
			_starImpls.add((StarMapStarControllerImpl) c);
		
		_galactic_radius = Game.Instance.Static.getDouble(Hash.SGalaxy, Hash.Radius);
		
		_scale = 10.0;
		_centerx = 0.0;
		_centery = 0.0;
		
		Image img = _da.createImage(_galactic_radius*2.0, _galactic_radius*2.0,
				StarMapResources.Instance.background());
		_map.addShape(img);
		
		_minorGraticuleLayer = _da.createGroup();
		_map.addShape(_minorGraticuleLayer);
		
		_majorGraticuleLayer = _da.createGroup();
		_map.addShape(_majorGraticuleLayer);
		
		_starIconLayer = _da.createGroup();
		_map.addShape(_starIconLayer);
		
		_starNameLayer = _da.createGroup();
		_map.addShape(_starNameLayer);
		
//		drawMinorGraticuleLayer(_minorGraticuleLayer);
		drawMajorGraticuleLayer(_majorGraticuleLayer);
		drawStarIcons(_starIconLayer);
		
		_map.setScale(_scale);
		_map.setTranslation(_centerx, _centery);
    }
	
	@Override
	public void onResize()
	{
		Widget parent = getParent();
		int w = parent.getOffsetWidth();
		int h = parent.getOffsetHeight();
		_da.setPixelSize(w, h);
	}
	
	private Path makeGrid(int count, int skip, double spacing)
	{
		Path p = _da.createPath();
		
		double r = _galactic_radius;
		double o = -r;
		
		p.begin();
		for (int i = 0; i < count; i++)
		{
			if ((i % skip) != 0)
			{
				p.moveTo(o, -r);
				p.lineTo(o, +r);
				
				p.moveTo(-r, o);
				p.lineTo(+r, o);
			}
			
			o += spacing;
		}
		p.end();
		
		return p;
	}
	
	private void drawMinorGraticuleLayer(Group g)
	{
		g.setClass("minorGraticule");
		
		double r = _galactic_radius;
		double r2 = r * 2.0;
		
		int lines = (int)(r * 10.0);
		Path p = makeGrid(lines, 10, r2 / (double)lines);
		g.addShape(p);
	}
	
	private void drawMajorGraticuleLayer(Group g)
	{
		g.setClass("majorGraticule");
		
		double r = _galactic_radius;
		double r2 = r * 2.0;
		
		int lines = (int)r;
		Path p = makeGrid(lines, (int)r, r2 / (double)lines);
		g.addShape(p);
	}
		
	private void star(HasShapes g, double cx, double cy, double r)
	{
		Circle circle1 = _da.createCircle(cx, cy, r*3.0/3.0);
		circle1.setOpacity(1.0/3.0);
		Circle circle2 = _da.createCircle(cx, cy, r*2.0/3.0);
		circle2.setOpacity(2.0/3.0);
		Circle circle3 = _da.createCircle(cx, cy, r*1.0/3.0);
		circle3.setOpacity(3.0/3.0);
		g.addShape(circle1);
		g.addShape(circle2);
		g.addShape(circle3);
	}
	
	private void drawStarIcons(Group g)
	{
		g.setClass("starIcon");
		
		for (StarMapStarControllerImpl impl : _starImpls)
		{
			StarMapStarController.StarData sd = impl.getStarData();
			
			double x = sd.x;
			double y = sd.y;
			
			star(g, x, y, 0.04);
		}
	}
	
	@Override
	public void onMouseWheel(MouseWheelEvent event)
	{
		if (event.isNorth())
			_scale *= 1.1;
		else
			_scale /= 1.1;
		_map.setScale(_scale);
	}
	
	@Override
	public void onMouseDown(MouseDownEvent event)
	{
		_dragstartx = event.getClientX();
		_dragstarty = event.getClientY();
		_dragdeltax = _dragdeltay = 0.0;
		
		if (_draghandler != null)
			_draghandler.removeHandler();
		_draghandler = addDomHandler(this, MouseMoveEvent.getType());
	}
	
	@Override
	public void onMouseMove(MouseMoveEvent event)
	{
		_dragdeltax = event.getClientX() - _dragstartx;
		_dragdeltay = event.getClientY() - _dragstarty;
		
		_map.setTranslation(_centerx+_dragdeltax, _centery+_dragdeltay);
		event.preventDefault();
	}
	
	@Override
	public void onMouseUp(MouseUpEvent event)
	{
		if (_draghandler != null)
		{
			_draghandler.removeHandler();
			_draghandler = null;
			
			_centerx += _dragdeltax;
			_centery += _dragdeltay;
		}
	}
	
	@Override
	public void onMouseOut(MouseOutEvent event)
	{
		onMouseUp(null);
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
