package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.database.RPCListener;
import com.cowlark.stellation3.common.database.RPCMonitorHandler;
import com.cowlark.stellation3.common.game.CompletionListener;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.gwt.S;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class RefreshWidget extends Composite
	implements RPCMonitorHandler
{
	
	private static MetaWidgetUiBinder uiBinder = GWT
	        .create(MetaWidgetUiBinder.class);
	
	interface MetaWidgetUiBinder extends UiBinder<Widget, RefreshWidget>
	{
	}
	
	private class WidgetTimer extends Timer
	{
		@Override
		public void run()
		{
			long delta = System.currentTimeMillis() - _lastRefresh;
			double deltaf = (double)delta / (double)S.REFRESHTIME;
		
			if (deltaf >= 1.0)
				refreshClicked(null);
			else
				redraw();
		}
	}
	
	@UiField
	ButtonPanel _refreshButton;
	
	@UiField
	ResizingCanvas _refreshCanvas;
	
	private WidgetTimer _timer;
	private long _lastRefresh;
	private boolean _waiting;
	private HandlerRegistration _rpcHandler;
	
	public RefreshWidget()
	{
		initWidget(uiBinder.createAndBindUi(this));
		_lastRefresh = System.currentTimeMillis();
	}
	
	@Override
	protected void onAttach()
	{
	    super.onAttach();
		
	    _rpcHandler = Game.Instance.RPCManager.addRPCMonitor(this);
	    
		_timer = new WidgetTimer();
		_timer.run();
		_timer.scheduleRepeating(250);
	}
	
	@Override
	protected void onDetach()
	{
		_timer.cancel();
	    super.onDetach();
	}
	
	private void redraw()
	{
		long delta = System.currentTimeMillis() - _lastRefresh;
		double deltaf = (double)delta / (double)S.REFRESHTIME;
		
		double w = _refreshCanvas.getOffsetWidth();
		double h = _refreshCanvas.getOffsetHeight();
		double w2 = w / 2.0;
		double h2 = h / 2.0;
		double r = Math.max(w2, h2) * 0.8;
		Context2d c = _refreshCanvas.getContext2d();
		
		c.save();
		c.clearRect(0, 0, w, h);
		
		c.setStrokeStyle("none");
		c.beginPath();
		if (_waiting)
		{
			c.setFillStyle("rgba(0, 255, 0, 0.5)");
			c.moveTo(w2, h2-r);
			c.arc(w2, h2, r, 0, 2.0*Math.PI); 
		}
		else
		{
			c.setFillStyle("rgba(0, 255, 255, 0.5)");
			c.moveTo(w2, h2);
			c.lineTo(w2, h2-r);
			c.arc(w2, h2, r, -0.5*Math.PI, 2.0*Math.PI*deltaf - 0.5*Math.PI);
			c.lineTo(w2, h2);
		}
		c.fill();
		
		c.drawImage(CanvasResources.Reload,
				(int)(w2 - CanvasResources.Reload.getWidth()/2),
				(int)(h2 - CanvasResources.Reload.getHeight()/2));
		
		c.restore();
	}
	
	@UiHandler("_refreshButton")
	void refreshClicked(ClickEvent event)
	{
		Game.Instance.RPCManager.postCommand(null, "Ping");
	}
	
	@Override
	public void onRPCBeginning()
	{
		_waiting = true;
		redraw();
	}
	
	@Override
	public void onRPCEnding()
	{
		_waiting = false;
		_lastRefresh = System.currentTimeMillis();
		redraw();
	}
}
