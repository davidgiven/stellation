package com.cowlark.stellation3.common.game;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Dialogue;
import com.cowlark.stellation3.common.controllers.DialogueHandler;
import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.controllers.TextFieldHandler;
import com.cowlark.stellation3.common.database.Database;
import com.cowlark.stellation3.common.database.RPCManager;
import com.cowlark.stellation3.common.database.Transport;
import com.google.gwt.user.client.Window;

public abstract class Game
{
	public static Game Instance;
	public static StarMap StarMap;
	
	public Database Database;
	public Transport Transport;
	public RPCManager RPCManager;
	
	public void start()
	{
		Database = new Database();
		Transport = createTransport();
		RPCManager = new RPCManager(Transport);
		
		showLoginScreen();
	}
	
	public void showLoginScreen()
	{
		new LoginDialogue();
	}
	
	public void login(String email, String password)
	{
		RPCManager.authenticate(email, password);
	}
	
	public void authenticationFailed()
	{
		Window.alert("Authentication failed!");
	}
	
	public void protocolError()
	{
		Window.alert("Protocol error!");
	}
	
	public void loggedIn(int playeroid)
	{
		RPCManager.doInitialSync();
	}
	
	public void initialSyncComplete()
	{
		Window.alert("done sync");
	}
	
	public abstract Dialogue showDialogue(ControllerGroup cg, DialogueHandler dh);
	public abstract StarMap showStarMap();
	public abstract Pane setLeftPane(ControllerGroup cg);
	public abstract Pane setRightPane(ControllerGroup cg);
	
	public abstract Transport createTransport();
	
	public abstract TextFieldController createTextFieldController(
			TextFieldHandler tfh, String label);
	public abstract TextFieldController createPasswordTextFieldController(
			TextFieldHandler tfh, String label);
	public abstract ButtonsController createButtonsController(
			ButtonsHandler bh, String... buttons);	
}
