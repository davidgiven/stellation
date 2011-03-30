package com.cowlark.stellation3.gwt.ui;

import java.util.Date;
import java.util.Formatter;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.markup.HasMarkup;
import com.cowlark.stellation3.common.markup.MarkupParser;
import com.cowlark.stellation3.common.model.SObject;
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
		Game.Instance.showObject(o);
	}
	
	private static PopupPanel _popup;
	private static Label _popupLabel;
	
	private static void showTimePopup(String times, NativeEvent event)
	{
		long timel = Long.parseLong(times);
		Date time = new Date(timel * 1000L);
		showPopup(S.TIME_FORMAT.format(time), event);
	}
	
	private static void showPopup(String text, NativeEvent event)
	{
		if (_popup == null)
		{
			_popup = new PopupPanel();
			_popupLabel = new Label();
			_popup.add(_popupLabel);
		}
		
		int x = event.getClientX();
		int y = event.getClientY();
		
		_popup.setPopupPosition(x+10, y+10);
		_popupLabel.setText(text);
		_popup.show();
	}
	
	private static void hidePopup()
	{
		if (_popup != null)
			_popup.hide();
	}
	
	private static native void publish() /*-{
		$wnd.showObject = @com.cowlark.stellation3.gwt.ui.MarkupLabelWidget::showLink(I);
		$wnd.showTimePopup = @com.cowlark.stellation3.gwt.ui.MarkupLabelWidget::showTimePopup(Ljava/lang/String;Lcom/google/gwt/dom/client/NativeEvent;);
		$wnd.showPopup = @com.cowlark.stellation3.gwt.ui.MarkupLabelWidget::showPopup(Ljava/lang/String;Lcom/google/gwt/dom/client/NativeEvent;);
		$wnd.hidePopup = @com.cowlark.stellation3.gwt.ui.MarkupLabelWidget::hidePopup();
	}-*/;
	
	static
	{
		publish();
	}
	
	private StringBuilder _rendering;
	
	public MarkupLabelWidget()
    {
    }
	
	public MarkupLabelWidget(String markup)
	{
		setMarkup(markup);
	}
	
	public void setMarkup(String markup)
	{
		_rendering = new StringBuilder();
		MarkupParser.render(markup, this);
		setHTML(_rendering.toString());
	}
	
	private String popup(String command)
	{
		return "onmousemove='"+command+"' onmouseover='"+command+"' onmouseout='hidePopup();'";
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
		_rendering.append("<span class='stardate' ");
		_rendering.append(popup(showtimepopup));
		_rendering.append(">");
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
	
	private void emitresource(String classid, String label, double value)
	{
		String s = "<span class='" +
			classid + "' " +
			popup("showPopup(\""+label+"\", event);") +
			">" + S.CARGO_FORMAT.format(value) + "</span>";
		_rendering.append(s);
	}
	
	@Override
	public void emitResources(double m, double a, double o)
	{
		emitresource("resourcesM", "Metal", m);
		emitresource("resourcesA", "Antimatter", a);
		emitresource("resourcesO", "Organics", o);
	}
}
