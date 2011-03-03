package com.cowlark.stellation3.common.game;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Dialogue;
import com.cowlark.stellation3.common.controllers.TextFieldController;

public class LoginDialogue implements ButtonsHandler
{
	private Game Game = com.cowlark.stellation3.common.game.Game.Instance;
	private Dialogue _dialogue;
	private TextFieldController _email;
	private TextFieldController _password;
	
	public LoginDialogue()
    {
		ControllerGroup cg = new ControllerGroup("Login");
		
		_email = Game.createTextFieldController(
				null, "Email address");
		_password = Game.createPasswordTextFieldController(
				null, "Password");
		
		cg.addController(_email);
		cg.addController(_password);
		
		ButtonsController buttons = Game.createButtonsController(
				this, "Register", "Login");
		cg.addController(buttons);
		
		_dialogue = Game.showDialogue(cg, null);
		
		_email.setStringValue("dg@cowlark.com");
		_password.setStringValue("fnord");
		//onButtonPressed(buttons, 1);
    }
	
	@Override
	public void onButtonPressed(ButtonsController bc, int which)
	{
		switch (which)
		{
			case 0: /* Register */
				break;
				
			case 1: /* Login */
			{
				String email = _email.getStringValue();
				String password = _password.getStringValue();
				_dialogue.closeDialogue();
				Game.login(email, password);
				break;
			}
		}
	}
}
