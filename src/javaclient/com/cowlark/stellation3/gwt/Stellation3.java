
package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.database.MessageListener;
import com.cowlark.stellation3.common.database.Reader;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

public class Stellation3 implements EntryPoint
{
	public void onModuleLoad()
	{
		Transport transport = new Transport("http://localhost/~dg/cgi-bin/stellation.cgi");
		
		MessageListener ml = new MessageListener()
		{
			@Override
			public void onMessageReceived(Reader reader)
			{
				Window.alert("received!");
			}
		};
		
		transport.sendMessage(ml,
				"Authenticate",
				"dg@cowlark.com",
				"fnord");
	}
}
