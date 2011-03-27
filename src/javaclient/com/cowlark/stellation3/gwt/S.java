package com.cowlark.stellation3.gwt;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class S
{
	public final static String CGIURL
		= "http://hilfy/~dg/cgi-bin/stellation.cgi";
	
	public final static long REFRESHTIME = 60 * 1000; // ms
	
	public final static NumberFormat COORD_FORMAT
		= NumberFormat.getFormat("0.0");
	public final static DateTimeFormat TIME_FORMAT
		= DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);
}
