/* Three-way vertical column panel.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/FullScreenPanel.java,v $
 * $Date: 2009/09/08 19:05:40 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.ui;

import java.util.HashMap;
import java.util.Map;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FullScreenPanel extends Composite implements ResizeHandler
{
	private static class Child
	{
		int x1, y1;
		int x2, y2;
	};
	
	private Map<Widget, Child> _children = new HashMap<Widget, Child>();
	private AbsolutePanel _panel = new AbsolutePanel();
	private HandlerRegistration _resizeHandler;
	
	public FullScreenPanel()
    {
		initWidget(_panel);
    }
	
	@Override
	protected void onLoad()
	{
	    super.onLoad();
	    _resizeHandler = Window.addResizeHandler(this);
		layout();
	}
	
	@Override
	protected void onUnload()
	{
		_resizeHandler.removeHandler();
	    super.onUnload();
	}
	
	public void add(Widget w, int x1, int y1, int x2, int y2)
	{
		Child child = new Child();
		child.x1 = x1;
		child.y1 = y1;
		child.x2 = x2;
		child.y2 = y2;
		_children.put(w, child);
		_panel.add(w);
		
		if (_resizeHandler != null)
			layout();
	}
	
	public void remove(Widget w)
	{
		_children.remove(w);
		_panel.remove(w);
		
		if (_resizeHandler != null)
			layout();
	}
	
	public void onResize(ResizeEvent event)
	{
		layout();
	}
	
	private int ord(int min, int max, int value)
	{
		if (value > 0)
			return min + value;
		return max + value;
	}
	
	private void layout()
	{
		int width = Window.getClientWidth();
		int height = Window.getClientHeight();
		setPixelSize(width, height);
		
		for (Map.Entry<Widget, Child> entry : _children.entrySet())
		{
			Child child = entry.getValue();
			int x1 = ord(0, width, child.x1);
			int y1 = ord(0, height, child.y1);
			int x2 = ord(0, width, child.x2);
			int y2 = ord(0, height, child.y2);
			
			int w = x2 - x1 + 1;
			int h = y2 - y1 + 1;
			
			Widget widget = entry.getKey();
			_panel.setWidgetPosition(widget, x1, y1);
			widget.setPixelSize(w, h);
		}
	}
}
