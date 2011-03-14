package com.cowlark.stellation3.gwt.ui;

import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.game.Game;
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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class StarMapImpl extends Composite implements 
	Pane, RequiresResize, MouseWheelHandler, MouseDownHandler,
	MouseUpHandler, MouseMoveHandler, MouseOutHandler
{
	private ControllerGroup _cg;
	private PaneHandler _ph;
	private Vector<StarMapStarControllerImpl> _starImpls;
	private AbsolutePanel _container;
	private AbsolutePanel _contents;
	private Image _background;
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
		_container = new AbsolutePanel();
		initWidget(_container);
		_contents = new AbsolutePanel();
		_container.add(_contents);
		
		addDomHandler(this, MouseWheelEvent.getType());
		addDomHandler(this, MouseDownEvent.getType());
		addDomHandler(this, MouseUpEvent.getType());
		addDomHandler(this, MouseOutEvent.getType());
		
		_background = new Image(StarMapResources.Instance.background().getURL());
		_contents.add(_background);
		
		_starImpls = new Vector<StarMapStarControllerImpl>();
		for (Controller c : cg)
		{
			StarMapStarControllerImpl cc = (StarMapStarControllerImpl) c;
			_starImpls.add(cc);
			_contents.add(cc);
		}
		
		_galactic_radius = Game.Instance.Static.getDouble(Hash.SGalaxy, Hash.Radius);
		
		_scale = 10.0;
		_centerx = 0.0;
		_centery = 0.0;
		
		placeItems();
    }
	
	@Override
	public void onResize()
	{
		Widget parent = getParent();
		int w = parent.getOffsetWidth();
		int h = parent.getOffsetHeight();
		_container.setPixelSize(w, h);
	}
	
	private void placeItems()
	{
		double backgroundsize = _galactic_radius * 2.0 * _scale;
		_background.setPixelSize((int)backgroundsize, (int)backgroundsize);
		
		for (StarMapStarControllerImpl cc : _starImpls)
		{
			cc.setScale(_scale * 0.01);
			StarMapStarController.StarData sd = cc.getStarData();
			double x = (_galactic_radius + sd.x)*_scale - cc.getOffsetX();
			double y = (_galactic_radius + sd.y)*_scale - cc.getOffsetY();
			_contents.setWidgetPosition(cc, (int)x, (int)y);
		}
	}
	
	@Override
	public void onMouseWheel(MouseWheelEvent event)
	{
		if (event.isNorth())
			_scale *= 1.1;
		else
			_scale /= 1.1;
		placeItems();
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
		
		event.preventDefault();
	}
	
	@Override
	public void onMouseMove(MouseMoveEvent event)
	{
		_dragdeltax = event.getClientX() - _dragstartx;
		_dragdeltay = event.getClientY() - _dragstarty;
		
		_container.setWidgetPosition(_contents, 
				(int)(_centerx+_dragdeltax), (int)(_centery+_dragdeltay));
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
