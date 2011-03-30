package com.cowlark.stellation3.gwt.controllers;

import java.util.List;
import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.gwt.GameImpl;
import com.cowlark.stellation3.gwt.ui.CanvasResources;
import com.cowlark.stellation3.gwt.ui.Point;
import com.cowlark.stellation3.gwt.ui.ResizingCanvas;
import com.google.gwt.canvas.dom.client.Context2d;
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
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;

public class StarMapImpl implements 
	Pane, ResizeHandler, MouseDownHandler, MouseUpHandler, MouseOutHandler,
	MouseMoveHandler, MouseWheelHandler, ScheduledCommand
{
	private PaneHandler _ph;
	private Vector<StarMapStarControllerImpl> _starImpls;
	private ResizingCanvas _backgroundCanvas;
	private ResizingCanvas _annotationCanvas;
	private Label _positionLabel;
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
	private Point _pointingAt;
	
	private static NumberFormat _coordFormat = NumberFormat.getFormat("0.0");
	
	public StarMapImpl(PaneHandler ph)
    {
		_ph = ph;
		
		_backgroundCanvas = GameImpl.Instance.Screen.BackgroundCanvas;
		_annotationCanvas = GameImpl.Instance.Screen.AnnotationCanvas;
		_positionLabel = GameImpl.Instance.Screen.BackgroundBottomLeftLabel;
		
		_annotationCanvas.addHandler(this, MouseWheelEvent.getType());
		_annotationCanvas.addHandler(this, MouseDownEvent.getType());
		_annotationCanvas.addHandler(this, MouseUpEvent.getType());
		_annotationCanvas.addHandler(this, MouseOutEvent.getType());
		_annotationCanvas.addHandler(this, MouseMoveEvent.getType());
		_backgroundCanvas.addResizeHandler(this);
		
		_starImpls = new Vector<StarMapStarControllerImpl>();
		
		_galactic_radius = Game.Instance.Static.getDouble(Hash.SGalaxy, Hash.Radius);
		
		_scale = 200.0;
		_centerx = 0.0;
		_centery = 0.0;
		
		_backgroundCanvas.onResize();
		_annotationCanvas.onResize();
    }
	
	@Override
	public void setTitle(String title)
	{
	}
	
	@Override
	public void updateControllers(List<Controller> controllers)
	{
		_starImpls.clear();
		for (Controller c : controllers)
			_starImpls.add((StarMapStarControllerImpl) c);
		
		deferredRedraw();
	}
	
	@Override
	public void onResize(ResizeEvent event)
	{
		redrawBackground();
	}
	
	private static ImageElement getStarForBrightness(double brightness)
	{
		int i = (int)brightness - 1;
		i = Math.max(i, 0);
		i = Math.min(i, 9);
		return CanvasResources.Star[i];
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
		redrawBackground();
		_redrawPending = false;
	}
	
	private void redrawBackground()
	{
		Context2d c = _backgroundCanvas.getContext2d();
		double w = _backgroundCanvas.getOffsetWidth();
		double h = _backgroundCanvas.getOffsetHeight();
		double mw = w / 2;
		double mh = h / 2;
		
		double pixel = 1.0/_scale;
		
		c.save();
		c.setFillStyle("black");
		c.fillRect(0, 0, w, h);
		c.setFillStyle("none");
		c.translate(mw, mh);
		c.scale(_scale, _scale);
		c.translate(_centerx, _centery);
		c.setLineWidth(1.0*pixel);
		
		double galaxysize = _galactic_radius * 1.5;
		double galaxysize2 = galaxysize * 2.0;
		
		c.drawImage(CanvasResources.Background,
				-galaxysize, -galaxysize,
				galaxysize2, galaxysize2);
		
		c.setStrokeStyle("white");
		double starsize = 0.0025 * _scale;
		double starsize2 = 2.0 * starsize;
		for (StarMapStarControllerImpl impl : _starImpls)
		{
			StarMapStarController.StarData sd = impl.getStarData();
			
			double x = sd.x;
			double y = sd.y;
			
			c.drawImage(getStarForBrightness(sd.brightness),
					x-starsize, y-starsize,
					starsize2, starsize2);
		}
		
		c.restore();
		updateLabels();
	}
	
	private void updateLabels()
	{
		StringBuilder sb = new StringBuilder();
		if (_pointingAt != null)
		{
			sb.append("Map x=");
			sb.append(_coordFormat.format(_pointingAt.x));
			sb.append(" y=");
			sb.append(_coordFormat.format(_pointingAt.y));
			sb.append(" ");
			sb.append("scale=");
			sb.append((int) _scale);
			sb.append(" pixels/parsec");
		}
		
		_positionLabel.setText(sb.toString());
	}
	
	private Point screenToCoord(double sx, double sy)
	{
		double mw = _backgroundCanvas.getOffsetWidth() / 2;
		double mh = _backgroundCanvas.getOffsetHeight() / 2;
		
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
		double mw = _backgroundCanvas.getOffsetWidth() / 2;
		double mh = _backgroundCanvas.getOffsetHeight() / 2;
		
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
		double mw = _backgroundCanvas.getOffsetWidth() / 2;
		double mh = _backgroundCanvas.getOffsetHeight() / 2;
		
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
		redrawBackground();
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
		
		_pointingAt = screenToCoord(screenx, screeny);
		updateLabels();
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
