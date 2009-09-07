/* Player registration dialogue.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/dialog/CreatePlayerDialog.java,v $
 * $Date: 2009/09/07 21:49:14 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.dialog;

import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.common.Authentication;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class CreatePlayerDialog extends DialogBox
		implements AsyncCallback<Void>
{
	private TextBox _uid;
	private TextBox _password;
	private TextBox _password2;
	private TextBox _name;
	private TextBox _empire;
	private Button _cancel;
	private Button _create;
	
	public CreatePlayerDialog()
    {
		super();
		
		setModal(true);
		setText("Create new player");
		
		Grid grid = new Grid(6, 2);
		add(grid);
		
		grid.setText(0, 0, "Email address:");
		_uid = new TextBox();
		grid.setWidget(0, 1, _uid);
		
		grid.setText(1, 0, "Password:");
		_password = new PasswordTextBox();
		grid.setWidget(1, 1, _password);
		
		grid.setText(2, 0, "Password (again):");
		_password2 = new PasswordTextBox();
		grid.setWidget(2, 1, _password2);
		
		grid.setText(3, 0, "Name of empire:");
		_empire = new TextBox();
		_empire.setText("The Generic Empire");
		grid.setWidget(3, 1, _empire);
		
		grid.setText(4, 0, "Visible name:");
		_name = new TextBox();
		_name.setText("Bob Starnudger");
		grid.setWidget(4, 1, _name);
		
		_cancel = new Button("Cancel",
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						hide();
						new LoginDialog().show();
					}
				}
		);
		grid.setWidget(5, 0, _cancel);
		
		_create = new Button("Create",
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						attemptToCreateUser();
					}
				}
		);
		grid.setWidget(5, 1, _create);
		
		center();
    }
	
	private void attemptToCreateUser()
	{
		String uid = _uid.getText();
		String password = _password.getText();
		String password2 = _password2.getText();
		String empire = _empire.getText();
		String name = _name.getText();
		
		if (!password.equals(password2))
		{
			Stellation2.alert("Your passwords don't match.");
			return;
		}
			
		_cancel.setEnabled(false);
		_create.setEnabled(false);
		Stellation2.Service.createUser(uid, password, empire, name, this);
	}
	
	public void onFailure(Throwable caught)
	{
		_cancel.setEnabled(true);
		_create.setEnabled(true);
	}
	
	public void onSuccess(Void result)
	{
		Authentication auth = new Authentication(_uid.getText(),
				_password.getText());
		hide();
		Stellation2.login(auth);
	}
}
