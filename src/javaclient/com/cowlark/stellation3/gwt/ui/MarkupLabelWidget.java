package com.cowlark.stellation3.gwt.ui;

import java.util.Date;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.markup.HasMarkup;
import com.cowlark.stellation3.common.markup.MarkupParser;
import com.cowlark.stellation3.common.model.SFleet;
import com.cowlark.stellation3.common.model.SObject;
import com.cowlark.stellation3.common.model.SStar;
import com.cowlark.stellation3.gwt.S;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class MarkupLabelWidget extends HTML implements HasMarkup
{
	private static void showLink(int oid)
	{
		SObject o = Game.Instance.Database.get(oid);
		switch (o.Class.get())
		{
			case SStar:
				Game.Instance.showStarDetails((SStar) o);
				break;
		}
	}
	
	private static PopupPanel _timePopup;
	private static Label _timePopupLabel;
	
	private static void showTimePopup(String times, NativeEvent event)
	{
		if (_timePopup == null)
		{
			_timePopup = new PopupPanel();
			_timePopupLabel = new Label();
			_timePopup.add(_timePopupLabel);
		}
		
		int x = event.getClientX();
		int y = event.getClientY();
		
		long timel = Long.parseLong(times);
		Date time = new Date(timel * 1000L);
		_timePopup.setPopupPosition(x+10, y+10);
		_timePopupLabel.setText(S.TIME_FORMAT.format(time));
		_timePopup.show();
	}
	
	private static void hideTimePopup()
	{
		if (_timePopup != null)
			_timePopup.hide();
	}
	
	private static native void publish() /*-{
		$wnd.showObject = @com.cowlark.stellation3.gwt.ui.MarkupLabelWidget::showLink(I);
		$wnd.showTimePopup = @com.cowlark.stellation3.gwt.ui.MarkupLabelWidget::showTimePopup(Ljava/lang/String;Lcom/google/gwt/dom/client/NativeEvent;);
		$wnd.hideTimePopup = @com.cowlark.stellation3.gwt.ui.MarkupLabelWidget::hideTimePopup();
	}-*/;
	
	static
	{
		publish();
	}
	
	private StringBuilder _rendering;
	
	public MarkupLabelWidget()
    {
    }
	
	public void setMarkup(String markup)
	{
		_rendering = new StringBuilder();
		MarkupParser.render(markup, this);
		setHTML(_rendering.toString());
	}
	
	@Override
	public void indent(int spaces)
	{
		getElement().getStyle().setPaddingLeft(spaces, Unit.EM);
	}
	
	@Override
	public void emitPlainText(String text)
	{
		for (int i=0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			switch (c)
			{
				case '<':
					_rendering.append("&lt;");
					break;
					
				case '>':
					_rendering.append("&gt;");
					break;
					
				case '&':
					_rendering.append("&amp;");
					break;
					
				default:
					_rendering.append(c);
			}
		}
	}
	
	@Override
	public void emitBoldText(String text)
	{
		_rendering.append("<b>");
		emitPlainText(text);
		_rendering.append("</b>");
	}
	
	@Override
	public void emitTime(long time)
	{
        double hours = time / 3600.0;
        long t1 = (int)(hours * 1000.0);
        long t3 = t1 % 1000;
        t1 /= 1000; /* to hours */
        long t2 = t1 % 1000;
        t1 /= 1000; /* to thousands of hours */
        
        StringBuilder sb = new StringBuilder();
        sb.append(t1);
        sb.append('.');
        if (t2 < 100)
                sb.append('0');
        if (t2 < 10)
                sb.append('0');
        sb.append(t2);
        sb.append('.');
        if (t3 < 100)
                sb.append('0');
        if (t3 < 10)
                sb.append('0');
        sb.append(t3);
		
        String showtimepopup = "showTimePopup(\"" + time + "\", event);";
		_rendering.append("<span class='stardate' onmousemove='");
		_rendering.append(showtimepopup);
		_rendering.append("' onmouseover='");
		_rendering.append(showtimepopup);
		_rendering.append("' onmouseout='hideTimePopup();'>");
		emitPlainText(sb.toString());
		_rendering.append("</span>");
	}
	
	@Override
	public void emitLink(String text, SObject object)
	{
		_rendering.append("<a href='javascript:;' onclick='showObject(");
		_rendering.append(object.Oid);
		_rendering.append(");'>");
		
		emitPlainText(text);
		
		_rendering.append("</a>");
	}
}
