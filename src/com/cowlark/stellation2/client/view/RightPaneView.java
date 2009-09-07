/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/Attic/RightPaneView.java,v $
 * $Date: 2009/09/07 22:28:13 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ui.WrappedLabel;
import com.cowlark.stellation2.common.model.CObject;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class RightPaneView<T extends CObject> extends AbstractView<T>
{
	private ScrollPanel _scrollpane = new ScrollPanel();
	private FlowPanel _panel = new FlowPanel();
	
	public RightPaneView(T object)
    {
		super(object);
		
		initWidget(_scrollpane);
		_scrollpane.setAlwaysShowScrollBars(false);
		_scrollpane.add(_panel);
		
		setStylePrimaryName("SimplePanel");
		_panel.setStylePrimaryName("PanelInner");
		
		add(new WrappedLabel("This is object #"+object.getId()+"!"));
    }
	
	public void add(Widget w)
    {
	    _panel.add(w);
    }
	
	@Override
	public void onChange(ChangeCallback cb)
	{
	}
}
