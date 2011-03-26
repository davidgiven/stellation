package com.cowlark.stellation3.common.game;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.TextFieldController;
import com.cowlark.stellation3.common.database.AuthenticationListener;

public class LoginSequencer
{
	private Pane _loginPane;
	private int _playeroid;
	
	public LoginSequencer()
    {
    }
	
	public void begin()
	{
		final TextFieldController emailController =
			Game.Instance.createTextFieldController(
					null, "Email address");
		final TextFieldController passwordController =
			Game.Instance.createPasswordTextFieldController(
					null, "Password");
		
		final ButtonsHandler bch =
			new ButtonsHandler()
			{
				@Override
				public void onButtonPressed(ButtonsController bc, int which)
				{
					switch (which)
					{
						case 0: /* Register */
							break;
							
						case 1: /* Login */
						{
							String email = emailController.getStringValue();
							String password = passwordController.getStringValue();
							_loginPane.closePane();
							userCredentialsEntered(email, password);
							break;
						}
					}
				}
			};
		
		final ButtonsController buttons =
			Game.Instance.createButtonsController(
					bch, "Register", "Login");
		
		Controller[] controllers = new Controller[]
		{
			emailController,
			passwordController,
			buttons
		};
		
		_loginPane = Game.Instance.showPane(controllers, PaneAspect.LOGIN, null,
				"Login");
		
		emailController.setStringValue("dg@cowlark.com");
		passwordController.setStringValue("fnord");
		
		/* Load images while the user is typing. */
		Game.Instance.loadUIData(null);
    }
	
	private void userCredentialsEntered(String email, String password)
	{
		Game.Instance.showProgress("Authenticating");
		
		Game.Instance.RPCManager.authenticate(email, password,
				new AuthenticationListener()
				{
					@Override
					public void onAuthenticationSucceeded(int playeroid)
					{
						_playeroid = playeroid;
						authenticationCompleted();
					}
					
					@Override
					public void onAuthenticationFailed()
					{
						begin();
					}
				}
		);
	}
	
	private void authenticationCompleted()
	{
		Game.Instance.showProgress("Downloading static data");
		Game.Instance.Static.downloadData(
				new CompletionListener()
				{
					@Override
					public void onCompletion()
					{
						staticDataDownloaded();
					}
				}
		);
	}
	
	private void staticDataDownloaded()
	{
		Game.Instance.showProgress("Syncing game database");
		Game.Instance.RPCManager.doInitialSync(
				new CompletionListener()
				{
					@Override
					public void onCompletion()
					{
						completedInitialSync();
					}
				}
		);
	}
				
	private void completedInitialSync()
	{
		Game.Instance.showProgress("Loading image data");
		Game.Instance.loadUIData(
				new CompletionListener()
				{
					@Override
					public void onCompletion()
					{
						completedLogin();
					}
				}
		);
	}
	
	private void completedLogin()
	{
		Game.Instance.showProgress(null);
		Game.Instance.loginCompleted(_playeroid);
	}
}
