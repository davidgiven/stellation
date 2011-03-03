
package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.gwt.ui.StarMapImpl;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Stellation3 implements EntryPoint
{
	public static Game Game;
	
	public void onModuleLoad()
	{
		Game = new GameImpl();
		com.cowlark.stellation3.common.game.Game.Instance = Game;
		
		Game.Transport = new TransportImpl("http://localhost/~dg/cgi-bin/stellation.cgi");
		
		Game.start();
		
//		int w = Window.getClientWidth();
//		int h = Window.getClientHeight();
//		
//		StarMapImpl starmap = new StarMapImpl(w, h);
//		RootLayoutPanel.get().add(starmap);
		
//		TransportImpl transport = new TransportImpl("http://localhost/~dg/cgi-bin/stellation.cgi");
//		
//		MessageListener ml = new MessageListener()
//		{
//			@Override
//			public void onMessageReceived(Reader reader)
//			{
//				Window.alert("received!");
//			}
//		};
//		
//		transport.sendMessage(ml,
//				"Authenticate",
//				"dg@cowlark.com",
//				"fnord");
	}
}
