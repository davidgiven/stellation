
package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.game.Game;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Stellation3 implements EntryPoint, UncaughtExceptionHandler
{
	public static Game Game;
	
	public void onModuleLoad()
	{
		GWT.setUncaughtExceptionHandler(this);
		
		Game = new GameImpl();
		com.cowlark.stellation3.common.game.Game.Instance = Game;
		
		Game.Transport = new TransportImpl("http://localhost/~dg/cgi-bin/stellation.cgi");
		
		Game.start();
	}
	
	@Override
	public void onUncaughtException(Throwable e)
	{
		PanicScreen ps = new PanicScreen(e);
		RootLayoutPanel.get().add(ps);
	}
}
