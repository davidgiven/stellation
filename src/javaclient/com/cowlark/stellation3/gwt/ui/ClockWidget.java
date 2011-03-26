package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.markup.MarkupBuilder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ClockWidget extends Composite
{
	private static ClockWidgetUiBinder uiBinder = GWT
	        .create(ClockWidgetUiBinder.class);
	
	interface ClockWidgetUiBinder extends UiBinder<Widget, ClockWidget>
	{
	}
	
	private class WidgetTimer extends Timer
	{
		@Override
		public void run()
		{
			long time = System.currentTimeMillis()/1000;
			
			MarkupBuilder mb = new MarkupBuilder();
			mb.emitTime(time);
			clock.setMarkup(mb.getMarkup());
		}
	}
	
	@UiField
	MarkupLabelWidget clock;
	
	private WidgetTimer _timer;
	
	public ClockWidget()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	protected void onAttach()
	{
	    super.onAttach();
		
		_timer = new WidgetTimer();
		_timer.run();
		_timer.scheduleRepeating(1000);
	}
	
	@Override
	protected void onDetach()
	{
		_timer.cancel();
	    super.onDetach();
	}
}
