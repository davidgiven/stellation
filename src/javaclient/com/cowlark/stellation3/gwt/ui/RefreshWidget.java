package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.gwt.S;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.elementparsers.CustomButtonParser;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.CustomButton.Face;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class RefreshWidget extends Composite
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
			
			double w = _refreshButton.getOffsetWidth();
			double h = _refreshButton.getOffsetHeight();
			double w2 = w / 2.0;
			double h2 = h / 2.0;
			double r = Math.max(w2, h2) * 0.8;
			Context2d c = _refreshButton.getContext2d();
			
			c.save();
			c.clearRect(0, 0, w, h);
			
			c.setFillStyle("rgba(255, 255, 255, 0.5)");
			c.setStrokeStyle("none");
			c.beginPath();
			c.moveTo(w2, h2);
			c.lineTo(w2, h2-r);
			c.arc(w2, h2, r, -0.5*Math.PI, 2.0*Math.PI*deltaf - 0.5*Math.PI);
			c.lineTo(w2, h2);
			c.fill();
			
			c.drawImage(CanvasResources.Reload,
					(int)(w2 - CanvasResources.Reload.getWidth()/2),
					(int)(h2 - CanvasResources.Reload.getHeight()/2));
			
			c.restore();
			
//			String dataurl = _canvas.toDataUrl("image/png");
//			Image img = new Image(dataurl);
//			_refreshButton.getUpFace().setImage(img);
//			_refreshButton.getDownFace().setImage(img);
//			_refreshButton.getUpHoveringFace().setImage(img);
//			_refreshButton.getDownHoveringFace().setImage(img);
			
			if (deltaf >= 1.0)
			{
				_lastRefresh = System.currentTimeMillis();
			}
		}
	}
	
	@UiField
	ResizingCanvas _refreshButton;
	
	private WidgetTimer _timer;
	private long _lastRefresh;
	
	public RefreshWidget()
	{
		initWidget(uiBinder.createAndBindUi(this));
		_lastRefresh = System.currentTimeMillis();
	}
	
	@Override
	protected void onAttach()
	{
	    super.onAttach();
		
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
}
