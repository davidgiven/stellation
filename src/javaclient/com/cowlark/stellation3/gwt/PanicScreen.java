package com.cowlark.stellation3.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PanicScreen extends Composite
{
	private static PanicScreenUiBinder uiBinder = GWT
	        .create(PanicScreenUiBinder.class);
	
	interface PanicScreenUiBinder extends UiBinder<Widget, PanicScreen>
	{
	}
	
	@UiField
	Label _stacktrace;
	
	@UiField
	Button _reloadButton;
	
	public PanicScreen(Throwable throwable)
	{
		initWidget(uiBinder.createAndBindUi(this));
		
		_stacktrace.setText(getStacktraceAsString(throwable));
	}
	
	@UiHandler("_reloadButton")
	void reloadClicked(ClickEvent event)
	{
		Window.Location.replace(Window.Location.getHref());
	}
	
	public static String getStacktraceAsString(Throwable t)
	{
		StackTraceElement[] trace = t.getStackTrace();
		StringBuilder sb = new StringBuilder();
		
		sb.append(t.toString());
		sb.append(": at\n");
		
		for (int i = 0; i < trace.length; i++)
		{
			sb.append(trace[i].toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
