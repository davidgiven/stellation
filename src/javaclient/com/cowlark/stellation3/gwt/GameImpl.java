package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.controllers.LabelController;
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
	public static GameImpl Instance;
	
	public Screen Screen;
	private DialogBox _progress;
	private StarMapImpl _starmap;
	
	public GameImpl()
    {
		Instance = this;
		Screen = new Screen();
		RootLayoutPanel.get().add(Screen);
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
	public Pane showPane(PaneAspect aspect, PaneHandler ph)
	{
		switch (aspect)
		{
			case LOGIN:
			{
				DialogueImpl d = new DialogueImpl(ph);
				d.show();
				return d;
			}
				
			case STARMAP:
			{
				assert(_starmap == null);
				_starmap = new StarMapImpl(ph);
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
		return new TransportImpl("http://hilfy/~dg/cgi-bin/stellation.cgi");
	}
	
	@Override
	public GroupTitleController createGroupTitleController(String title)
	{
	    return new GroupTitleControllerImpl(title);
	}
	
	@Override
	public LabelController createLabelController(String label)
	{
	    return new LabelControllerImpl(label);
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
