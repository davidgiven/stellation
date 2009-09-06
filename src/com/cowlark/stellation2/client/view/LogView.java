/* The Intelligence pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/LogView.java,v $
 * $Date: 2009/09/06 22:17:53 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import java.util.Map;
import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.common.Utils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollPanel;

public class LogView extends Composite implements ChangeCallback
{
	private ScrollPanel _scrollpanel = new ScrollPanel();
	private FlowPanel _panel = new FlowPanel();
	private Grid _data = new Grid();
	
	public LogView()
    {
		initWidget(_scrollpanel);
		setStylePrimaryName("SimplePanel");
		
		_scrollpanel.setAlwaysShowScrollBars(false);
		_scrollpanel.add(_panel);
		
		_panel.setStylePrimaryName("PanelInner");
		_panel.add(_data);
		_data.setStylePrimaryName("LogGrid");
		_data.addStyleName("Text");
    }

	@Override
	protected void onLoad()
	{
	    super.onLoad();   
	    ClientDB.addLogChangeCallback(this);
	    onChange(this);
	}
	
	@Override
	protected void onUnload()
	{
		ClientDB.removeLogChangeCallback(this);
	    super.onUnload();
	}
	
	public void onChange(ChangeCallback cb)
	{
		int size = ClientDB.getLogs().size();		
		_data.resize(size, 2);
		
		int row = 0;
		for (Map.Entry<Long, String> entry : ClientDB.getLogs())
		{
			long t = entry.getKey();
			_data.setText(row, 0, Utils.renderTime(t));
			_data.setText(row, 1, entry.getValue());
			row++;
		}
		
		_scrollpanel.scrollToTop();
	}
}
