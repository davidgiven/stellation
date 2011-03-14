package com.cowlark.stellation3.gwt.ui;

import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.game.Game;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.ImageElement;
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
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class StarMapImpl extends Composite implements 
	Pane, RequiresResize, MouseDownHandler, MouseUpHandler, MouseOutHandler,
	MouseMoveHandler, MouseWheelHandler, ScheduledCommand
{
	private ControllerGroup _cg;
	private PaneHandler _ph;
	private Vector<StarMapStarControllerImpl> _starImpls;
	private AbsolutePanel _panel;
	private Label _label;
	private GWTCanvas _canvas;
	private double _galactic_radius;
	private double _scale;
	private double _centerx;
	private double _centery;
	private boolean _dragging;
	private double _dragcenterx;
	private double _dragcentery;
	private double _dragstartx;
	private double _dragstarty;
	private double _dragdeltax;
	private double _dragdeltay;
	private boolean _redrawPending;
	
	private static NumberFormat _coordFormat = NumberFormat.getFormat("0.0");
	
	public StarMapImpl(ControllerGroup cg, PaneHandler ph)
    {
		_cg = cg;
		_ph = ph;
		
		_panel = new AbsolutePanel();
		_canvas = new GWTCanvas();
		_panel.add(_canvas, 0, 0);
		_label = new Label();
		_panel.add(_label, 0, 0);
		initWidget(_panel);
		
		addDomHandler(this, MouseWheelEvent.getType());
		addDomHandler(this, MouseDownEvent.getType());
		addDomHandler(this, MouseUpEvent.getType());
		addDomHandler(this, MouseOutEvent.getType());
		addDomHandler(this, MouseMoveEvent.getType());
		
		_starImpls = new Vector<StarMapStarControllerImpl>();
		for (Controller c : cg)
			_starImpls.add((StarMapStarControllerImpl) c);
		
		_galactic_radius = Game.Instance.Static.getDouble(Hash.SGalaxy, Hash.Radius);
		
		_scale = 10.0;
		_centerx = 0.0;
		_centery = 0.0;
		
		_label.setStylePrimaryName("starmapLabel");
		redraw();
    }
	
	@Override
	public void onResize()
	{
		Widget parent = getParent();
		int w = parent.getOffsetWidth();
		int h = parent.getOffsetHeight();
		_panel.setPixelSize(w, h);
		_canvas.resize(w, h);
		redraw();
	}
	
	private static ImageElement getStarForBrightness(double brightness)
	{
		int i = (int)brightness - 1;
		i = Math.max(i, 0);
		i = Math.min(i, 9);
		return UIResources.Star[i];
	}
	
	private void deferredRedraw()
	{
		if (_redrawPending)
			return;
		
		_redrawPending = true;
		Scheduler.get().scheduleDeferred(this);
	}
	
	@Override
	public void execute()
	{
		redraw();
		_redrawPending = false;
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
		_canvas.translate(_centerx, _centery);
		_canvas.setLineWidth(1.0*pixel);
		
		double galaxysize = _galactic_radius * 1.5;
		double galaxysize2 = galaxysize * 2.0;
		
		_canvas.drawImage(UIResources.Background,
				-galaxysize, -galaxysize,
				galaxysize2, galaxysize2);
		
		_canvas.setStrokeStyle(Color.WHITE);
		double starsize = 0.0025 * _scale;
		double starsize2 = 2.0 * starsize;
		for (StarMapStarControllerImpl impl : _starImpls)
		{
			StarMapStarController.StarData sd = impl.getStarData();
			
			double x = sd.x;
			double y = sd.y;
			
			_canvas.drawImage(getStarForBrightness(sd.brightness),
					x-starsize, y-starsize,
					starsize2, starsize2);
		}
		
		_canvas.restoreContext();
	}
	
	private Point screenToCoord(double sx, double sy)
	{
		double mw = _canvas.getCoordWidth() / 2;
		double mh = _canvas.getCoordHeight() / 2;
		
		sx -= mw;
		sy -= mh;
		
		sx /= _scale;
		sy /= _scale;
		
		sx -= _centerx;
		sy -= _centery;
		
		return new Point(sx, sy);
	}
	
	private Point coordToScreen(double cx, double cy)
	{
		double mw = _canvas.getCoordWidth() / 2;
		double mh = _canvas.getCoordHeight() / 2;
		
		cx += _centerx;
		cy += _centery;
		
		cx *= _scale;
		cy *= _scale;
		
		cx += mw;
		cy += mh;
		
		return new Point(cx, cy);
	}
	
	@Override
	public void onMouseWheel(MouseWheelEvent event)
	{
		double mw = _canvas.getCoordWidth() / 2;
		double mh = _canvas.getCoordHeight() / 2;
		double deltax = event.getClientX() - mw;
		double deltay = event.getClientY() - mh;
		
		/* After scaling, we want this point to be at the same place on the
		 * screen. */
		
		Point pointingAt = screenToCoord(event.getClientX(), event.getClientY());
		
		if (event.isNorth())
			_scale *= 1.3;
		else
			_scale /= 1.3;
		
		/* Where has the point moved to? */
		
		Point incorrectPointingAt = screenToCoord(event.getClientX(), event.getClientY());
		
		/* Adjust centerx accordingly. */
		
		_centerx += incorrectPointingAt.x - pointingAt.x;
		_centery += incorrectPointingAt.y - pointingAt.y;
		redraw();
	}

	@Override
	public void onMouseDown(MouseDownEvent event)
	{
		_dragcenterx = _centerx;
		_dragcentery = _centery;
		_dragstartx = event.getClientX();
		_dragstarty = event.getClientY();
		_dragdeltax = _dragdeltay = 0.0;
		
		_dragging = true;
		
		event.preventDefault();
	}
	
	@Override
	public void onMouseMove(MouseMoveEvent event)
	{
		int screenx = event.getClientX();
		int screeny = event.getClientY();
		
		if (_dragging)
		{
			_dragdeltax = screenx - _dragstartx;
			_dragdeltay = screeny - _dragstarty;
			
			_centerx = _dragcenterx + _dragdeltax / _scale;
			_centery = _dragcentery + _dragdeltay / _scale;
			event.preventDefault();
			
			deferredRedraw();
		}
		
		StringBuilder sb = new StringBuilder();
		Point p = screenToCoord(screenx, screeny);
		sb.append("Map x=");
		sb.append(_coordFormat.format(p.x));
		sb.append(" y=");
		sb.append(_coordFormat.format(p.y));
		
		_label.setText(sb.toString());
	}
	
	@Override
	public void onMouseUp(MouseUpEvent event)
	{
		_dragging = false; 
		
		if (event != null)
			event.preventDefault();
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
