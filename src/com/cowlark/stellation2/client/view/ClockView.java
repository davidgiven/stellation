/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/ClockView.java,v $
 * $Date: 2009/09/06 22:16:32 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.common.Utils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClockView extends Composite
{
	private VerticalPanel _panel = new VerticalPanel();
	private Label _descriptionLabel = new Label("GALACTIC STANDARD TIME");
	private Label _timeLabel = new Label();
	
	private Timer _timer = new Timer()
	{
		public void run()
		{
			update();
		}
	};
	
	public ClockView()
    {
		initWidget(_panel);
		setStylePrimaryName("SimplePanel");
		
		_descriptionLabel.addStyleName("ClockLabel");
		_timeLabel.addStyleName("Clock");

		_panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		_panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		_panel.add(_descriptionLabel);
		_panel.add(_timeLabel);
		
		update();
    }
	
	@Override
	protected void onLoad()
	{
		_timer.scheduleRepeating(3600);
	    super.onLoad();
	}
	
	@Override
	protected void onUnload()
	{
	    super.onUnload();
	    _timer.cancel();
	}
	
	private void update()
	{
		long serverTime = System.currentTimeMillis() + Stellation2.getTimeDelta();
		_timeLabel.setText(Utils.renderTime(serverTime));
	}
}
