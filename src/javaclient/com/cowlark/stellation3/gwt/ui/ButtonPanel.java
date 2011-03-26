package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;

public class ButtonPanel extends FocusPanel
{
	private boolean _hovered;
	private boolean _pressed;
	private boolean _disabled;
	private boolean _captured;
	
	public ButtonPanel()
    {
	    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS
	        | Event.KEYEVENTS);
    }
	
	private void updateStyle()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("gwt-PushButton-");
		if (_pressed)
			sb.append("down");
		else
			sb.append("up");
		
		if (_disabled)
			sb.append("-disabled");
		else if (_hovered)
			sb.append("-hovering");
		
		setStylePrimaryName(sb.toString());
	}
	
	@Override
	protected void onAttach()
	{
	    super.onAttach();
	    updateStyle();
	}
	
	@Override
	protected void onDetach()
	{
	    super.onDetach();
	}
	
	public void onClick()
	{
		fireEvent(new ClickEvent() {});
	}
	
	@Override
	public void onBrowserEvent(Event event)
	{
		if (_disabled)
			return;

		int type = DOM.eventGetType(event);
		switch (type)
		{
			case Event.ONMOUSEDOWN:
				if (event.getButton() == Event.BUTTON_LEFT)
				{
					_pressed = true;
					setFocus(true);
					DOM.setCapture(getElement());
					_captured = true;
					// Prevent dragging (on some browsers);
					DOM.eventPreventDefault(event);
				}
				break;
				
			case Event.ONMOUSEUP:
				if (_captured)
				{
					_captured = false;
					DOM.releaseCapture(getElement());
//					if (_hovered && (event.getButton() == Event.BUTTON_LEFT))
//						onClick();
				}
				_pressed = false;
				break;
				
			case Event.ONMOUSEMOVE:
				if (_captured)
					DOM.eventPreventDefault(event);
				break;
				
			case Event.ONMOUSEOUT:
				Element to = DOM.eventGetToElement(event);
				if (DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event))
						&& (to == null || !DOM.isOrHasChild(getElement(), to)))
					_hovered = false;
				break;
				
			case Event.ONMOUSEOVER:
				if (DOM.isOrHasChild(getElement(), DOM.eventGetTarget(event)))
					_hovered = true;
				break;
				
			case Event.ONLOSECAPTURE:
				if (_captured)
					_captured = false;
				break;
		}

		super.onBrowserEvent(event);

		// Synthesize clicks based on keyboard events AFTER the normal key handling.
		if ((event.getTypeInt() & Event.KEYEVENTS) != 0)
		{
			char keyCode = (char) DOM.eventGetKeyCode(event);
			switch (type)
			{
				case Event.ONKEYUP:
					if (keyCode == ' ')
						onClick();
					break;
					
				case Event.ONKEYPRESS:
					if (keyCode == '\n' || keyCode == '\r')
						onClick();
			          break;
			}
		}
		
		updateStyle();
	}
}
