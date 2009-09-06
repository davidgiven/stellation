/* Three-way vertical column panel.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/PaddedPanel.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PaddedPanel extends Composite
{
	private SimplePanel _container = new SimplePanel();
	private Widget _child;
	
	public PaddedPanel()
    {
		initWidget(_container);
		//setSize("100%", "100%");
		_container.setStylePrimaryName("PaddedPanel");
    }
	
	public PaddedPanel(Widget child)
	{
		this();
		add(child);
	}
	
	public void add(Widget child)
	{
		if (_child != null)
			_child.removeFromParent();
		_child = child;
		_container.add(_child);
	}
}
