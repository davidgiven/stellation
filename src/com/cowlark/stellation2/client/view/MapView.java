/* Implements the map.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/MapView.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.client.ui.ObjectWatcherComposite;
import com.cowlark.stellation2.client.ui.canvas.Canvas;
import com.cowlark.stellation2.client.ui.canvas.Glyph;
import com.cowlark.stellation2.client.ui.canvas.HLineGlyph;
import com.cowlark.stellation2.client.ui.canvas.RootGlyph;
import com.cowlark.stellation2.client.ui.canvas.TextGlyph;
import com.cowlark.stellation2.client.ui.canvas.VLineGlyph;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CGalaxy;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Label;

public class MapView extends ObjectWatcherComposite<CGalaxy>
		implements MouseDownHandler, MouseMoveHandler, MouseUpHandler
{
	public class MapNode extends ObjectWatcherComposite<CStar>
			implements ClickHandler, MouseOverHandler, MouseOutHandler
	{
		private Label _label;
		
		public MapNode(CStar star)
        {
			super(star);

			_label = new Label();
			_label.setStyleName("MapNode");
			_label.addClickHandler(this);
			_label.addMouseOverHandler(this);
			_label.addMouseOutHandler(this);
			initWidget(_label);
        }
		
		public void onChange(ChangeCallback cb)
		{
			try
			{
				CStar star = getObject();
				_label.setText(star.getName());
			}
			catch (OutOfScopeException e)
			{
				removeFromParent();
			}
		}
		
		public void onClick(ClickEvent event)
		{
			try
			{
				onSelectStar(getObject());
				onMouseOut(null);
			}
			catch (OutOfScopeException e)
			{
			}
		}
		
		public void onMouseOver(MouseOverEvent event)
		{
			try
			{
				onHoverStar(getObject());
				//_label.setStyleName("MapNode-Highlight");
			}
			catch (OutOfScopeException e)
			{
			}
		}
		
		public void onMouseOut(MouseOutEvent event)
		{
			try
			{
				onUnhoverStar(getObject());
				//_label.setStyleName("MapNode");
			}
			catch (OutOfScopeException e)
			{
			}
		}
	};
	
	private class StarGlyph extends TextGlyph implements EventListener
	{
		private DBRef<CStar> _star;
		
		public StarGlyph(CStar star)
        {
			super(star.getName());
			_star = new DBRef<CStar>(star);
			addClass("MapNode");
			
			Element e = getElement();
			DOM.setEventListener(e, this);
			DOM.sinkEvents(e, Event.MOUSEEVENTS);
        }
		
		public void onBrowserEvent(Event event)
		{
			CStar star = _star.get();
			if (star == null)
				return;
			
			switch (DOM.eventGetType(event))
			{
				case Event.ONMOUSEOVER:
					onHoverStar(star);
					break;
					
				case Event.ONMOUSEOUT:
					onUnhoverStar(star);
					break;
					
				case Event.ONMOUSEDOWN:
					break;
					
				case Event.ONMOUSEUP:
					onSelectStar(star);
					onUnhoverStar(star);
					return;
					
				default:
					return;
			}
			
			DOM.eventCancelBubble(event, true);
		}
	};
	
	private Canvas _canvas = new Canvas();
	private Glyph _map = new RootGlyph();
	private TextGlyph _status = new TextGlyph("");
	private DBRef<CStar> _hovered = new DBRef<CStar>();
	private double _pixelsPerParsec = 50.0;
	private double _mouseX;
	private double _mouseY;
	
	private int _mapX;
	private int _mapY;
	private boolean _dragging = false;
	private int _dragX;
	private int _dragY;
	
	public MapView()
    {
		super(Stellation2.getUniverse().getGalaxy());
	
		initWidget(_canvas);
		setStylePrimaryName("SimplePanel");
		
		//_vpanel.add(_canvas);
		_canvas.addStyleName("MapView");
		_canvas.addMouseDownHandler(this);
		_canvas.addMouseMoveHandler(this); 
		_canvas.addMouseUpHandler(this);
		
		_status.addClass("MapStatus");
		_mapX = _mapY = 0;
		
		Glyph g = _canvas.getRootGlyph();
		g.add(0, 0, _map);
		g.add(0, 0, _status);
    }
	
	public void onChange(ChangeCallback cb)
	{
		int size = (int)(S.GALAXY_RADIUS * 2.0 * _pixelsPerParsec);
		
		_map.clear();

		for (int i = -S.GALAXY_RADIUS; i <= S.GALAXY_RADIUS; i += 1)
		{
			int pos = (int)((double)(i + S.GALAXY_RADIUS) * _pixelsPerParsec);
			
			String style;
			if (i == 0)
				style = "MapCentreGraticule";
			else
				style = "MapNormalGraticule";
			
			_map.add(0, pos,
					new HLineGlyph(size)
						.addClass(style)
				);
			_map.add(pos, 0,
					new VLineGlyph(size)
						.addClass(style)
				);
		}
		
		
		try
		{
			CGalaxy galaxy = getObject();
			for (CObject o : galaxy)
			{
				CStar star = o.toStar();
				if (star != null)
				{
					int x = (int)((star.getX() + S.GALAXY_RADIUS) * _pixelsPerParsec);
					int y = (int)((star.getY() + S.GALAXY_RADIUS) * _pixelsPerParsec);
	
					Glyph g = new StarGlyph(star);					
					_map.add(x+6, y-4, g);
					
					g = new HLineGlyph(7).addClass("MapStarMarker");
					_map.add(x-3, y, g);
						
					g = new VLineGlyph(7).addClass("MapStarMarker");
					_map.add(x, y-3, g);
				}
			}
		}
		catch (OutOfScopeException e)
		{
		}
	}
	
	public void onMouseDown(MouseDownEvent event)
	{
		_dragging = true;
		_dragX = event.getX();
		_dragY = event.getY();
	}
	
	public void onMouseMove(MouseMoveEvent event)
	{
		if (_dragging)
		{
			int x = event.getX();
			int y = event.getY();
			
			_mapX += x - _dragX;
			_mapY += y - _dragY;
			_map.setPos(_mapX, _mapY);
			
			_dragX = x;
			_dragY = y;
		}
		else
		{
			/* Pixels from top-left of widget. */
			_mouseX = event.getX();
			_mouseY = event.getY();
			
			/* Pixels from top-left of map. */
			_mouseX -= _mapX;
			_mouseY -= _mapY;
			
			/* Parsecs from top-left of map. */
			_mouseX /= _pixelsPerParsec;
			_mouseY /= _pixelsPerParsec;
			
			/* Parsecs from centre of map. */
			_mouseX -= S.GALAXY_RADIUS;
			_mouseY -= S.GALAXY_RADIUS;
			
			updateStatus();
		}
	}
	
	public void onMouseUp(MouseUpEvent event)
	{
		_dragging = false;
	}
	
//	public void onMouseMove(int x, int y)
//	{
//		if (_dragging)
//		{
//			int deltax = x - _dragX;
//			int deltay = y - _dragY;
//			
//			_scrollpanel.setHorizontalScrollPosition(
//					_scrollpanel.getHorizontalScrollPosition() - deltax);
//			_scrollpanel.setScrollPosition(
//					_scrollpanel.getScrollPosition() - deltay);
//			
//			_dragX = x;
//			_dragY = y;
//		}
//		else
//		{
//			if (_pixelsPerParsec != 0.0)
//			{
//				x -= _map.getAbsoluteLeft();
//				y -= _map.getAbsoluteTop();
//				
//				_mouseX = (x / _pixelsPerParsec) - S.GALAXY_RADIUS;
//				_mouseY = (y / _pixelsPerParsec) - S.GALAXY_RADIUS;
//				updateStatus();
//			}
//		}
//	}
	
//	@Override
//	public void onBrowserEvent(Event event)
//	{
//		switch (DOM.eventGetType(event))
//		{
//			case Event.ONMOUSEDOWN:
//				onMouseDown(event.getScreenX(), event.getScreenY());
//				break;
//				
//			case Event.ONMOUSEMOVE:
//				onMouseMove(event.getScreenX(), event.getScreenY());
//				break;
//				
//			case Event.ONMOUSEUP:
//				onMouseUp(event.getScreenX(), event.getScreenY());
//				break;
//				
//			case Event.ONMOUSEWHEEL:
//			{
//				double factor = (event.getMouseWheelVelocityY() < 0.0)
//					? 2.0 : 0.5;
//				_pixelsPerParsec *= factor;
//				onChange(this);
//			}
//		}
//		
//	    super.onBrowserEvent(event);
//	}
	
	private void updateStatus()
	{
		double x, y;
		
		if (_hovered.isNull())
		{
			x = _mouseX;
			y = _mouseY;
		}
		else
		{
			CStar star = _hovered.get();
			x = star.getX();
			y = star.getY();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(S.COORD_FORMAT.format(x));
		sb.append(", ");
		sb.append(S.COORD_FORMAT.format(y));
		sb.append(")");
		
		if (!_hovered.isNull())
		{
			sb.append(" ");
			sb.append(_hovered.get().getName());
		}

		_status.setText(sb.toString());
	}
	
	private void onSelectStar(CStar star)
	{
		final long id = star.getId();
		
		DeferredCommand.addCommand(
				new Command()
				{
					public void execute()
					{
						CStar star = (CStar) Database.get(id);
						if (star != null)
							Stellation2.showStarViewer(star);
					}
				}
		);
	}
	
	private void onHoverStar(CStar star)
	{
		_hovered = new DBRef<CStar>(star);
		updateStatus();
	}
	
	private void onUnhoverStar(CStar star)
	{
		_hovered = new DBRef<CStar>();
		updateStatus();
	}
}
