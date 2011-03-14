package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.ControllerGroupCollection;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.controllers.StarMapStarHandler;
import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.cowlark.stellation3.common.database.Transport;
import com.cowlark.stellation3.common.game.CompletionListener;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.gwt.ui.Screen;
import com.cowlark.stellation3.gwt.ui.StarMapImpl;
import com.cowlark.stellation3.gwt.ui.StarMapStarControllerImpl;
import com.cowlark.stellation3.gwt.ui.UIResources;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GameImpl extends Game
{
	private Screen _screen;
	private DialogBox _progress;
	private StarMapImpl _starmap;
	
	public GameImpl()
    {
		_screen = new Screen();
		RootLayoutPanel.get().add(_screen);
    }
	
	@Override
	public void loadUIData(CompletionListener listener)
	{
		UIResources.waitForLoad(listener);
	}
	
	@Override
	public void showProgress(String message)
	{
		if (_progress != null)
		{
			_progress.hide();
			_progress = null;
		}
		
		if (message != null)
		{
			_progress = new DialogBox();
			_progress.setText(message);
			_progress.center();
			_progress.show();
		}
	}
	
	@Override
	public Pane showPane(ControllerGroupCollection cgc, PaneAspect aspect, PaneHandler ph)
	{
		switch (aspect)
		{
			case LOGIN:
			{
				DialogueImpl d = new DialogueImpl(cgc.getSingleton(), ph);
				d.show();
				return d;
			}
				
			case STARMAP:
			{
				assert(_starmap == null);
				_starmap = new StarMapImpl(cgc.getSingleton(), ph);
				_screen.setStarmap(_starmap);
				return _starmap;
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
	        ButtonsHandler bh, String... strings)
	{
	    return new ButtonsControllerImpl(bh, strings);
	}
	
	@Override
	public StarMapStarController createStarMapStarController(
	        StarMapStarHandler smsh, StarMapStarController.StarData sd)
	{
	    return new StarMapStarControllerImpl(smsh, sd);
	}
}
