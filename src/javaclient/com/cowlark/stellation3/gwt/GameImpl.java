package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Dialogue;
import com.cowlark.stellation3.common.controllers.DialogueHandler;
import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.cowlark.stellation3.common.database.Transport;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.game.Pane;
import com.cowlark.stellation3.common.game.StarMap;

public class GameImpl extends Game
{
	@Override
	public Dialogue showDialogue(ControllerGroup cg, DialogueHandler dh)
	{
		return new DialogueImpl(cg, dh);
	}
	
	@Override
	public StarMap showStarMap()
	{
	    // TODO Auto-generated method stub
	    return null;
	}
	
	@Override
	public Pane setLeftPane(ControllerGroup cg)
	{
	    // TODO Auto-generated method stub
	    return null;
	}
	
	@Override
	public Pane setRightPane(ControllerGroup cg)
	{
	    // TODO Auto-generated method stub
	    return null;
	}
	
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
