package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.cowlark.stellation3.common.database.Transport;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.gwt.ui.Screen;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GameImpl extends Game
{
	private Screen _screen;
	
	public GameImpl()
    {
		_screen = new Screen();
		RootLayoutPanel.get().add(_screen);
    }
	
	@Override
	public Pane showPane(ControllerGroup cg, PaneAspect aspect, PaneHandler ph)
	{
		switch (aspect)
		{
			case LOGIN:
			{
				DialogueImpl d = new DialogueImpl(cg, ph);
				d.show();
				return d;
			}
				
			default:
				assert false;
		}
		
		return null;
	}
	
//	@Override
//	public StarMap createStarMap()
//	{
//		StarMapImpl sm = new StarMapImpl();
//		_screen.setStarmap(sm);
//	    return sm;
//	}
	
	@Override
	public Transport createTransport()
	{
		return new TransportImpl("http://localhost/~dg/cgi-bin/stellation.cgi");
	}
	
	@Override
	public TextFieldController createTextFieldController(
			TextFieldHandler tfh, String label)
	{
		return new TextFieldControllerImpl(tfh, label, false);
	}
	
	@Override
	public TextFieldController createPasswordTextFieldController(
			TextFieldHandler tfh, String label)
	{
		return new TextFieldControllerImpl(tfh, label, true);
	}
	
	@Override
	public ButtonsController createButtonsController(
	        ButtonsHandler bh, String[] strings)
	{
	    return new ButtonsControllerImpl(bh, strings);
	}
}
