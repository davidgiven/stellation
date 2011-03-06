package com.cowlark.stellation3.common.game;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.cowlark.stellation3.common.database.Database;
import com.cowlark.stellation3.common.database.RPCManager;
import com.cowlark.stellation3.common.database.Transport;
import com.cowlark.stellation3.common.model.SGalaxy;
import com.cowlark.stellation3.common.model.SPlayer;
import com.cowlark.stellation3.common.model.SUniverse;
import com.google.gwt.user.client.Window;

public abstract class Game
{
	public static Game Instance;
	
	public Database Database;
	public Transport Transport;
	public Static Static;
	public RPCManager RPCManager;
	public SUniverse Universe;
	public SGalaxy Galaxy;
	public SPlayer Player;
	public StarMap StarMap;
	
	public void start()
	{
		Database = new Database();
		Transport = createTransport();
		Static = new Static(Transport);
		RPCManager = new RPCManager(Transport);
		
		LoginSequencer ls = new LoginSequencer();
		ls.begin();
	}
	
	public void protocolError()
	{
		Window.alert("Protocol error!");
	}
	
	public void loginCompleted(int playeroid)
	{
		Universe = (SUniverse) Database.get(1);
		Galaxy = Universe.Galaxy.<SGalaxy>get();
		Player = (SPlayer) Database.get(playeroid);
		
//		StarMap = createStarMap();
	}
	
	public abstract Transport createTransport();
	
	public abstract Pane showPane(ControllerGroup cg,
			PaneAspect aspect, PaneHandler cgh);
	
	public abstract TextFieldController createTextFieldController(
			TextFieldHandler tfh, String label);
	public abstract TextFieldController createPasswordTextFieldController(
			TextFieldHandler tfh, String label);
	public abstract ButtonsController createButtonsController(
			ButtonsHandler bh, String... buttons);	
}
