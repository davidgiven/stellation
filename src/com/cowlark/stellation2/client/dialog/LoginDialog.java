/* Player login dialogue.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/dialog/LoginDialog.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.dialog;

import com.cowlark.stellation2.client.Stellation2;
import com.cowlark.stellation2.common.Authentication;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class LoginDialog extends DialogBox
{
	public LoginDialog()
    {
		super();
		
		setModal(true);
		setText("Login");
		
		Grid grid = new Grid(3, 2);
		add(grid);
		
		grid.setText(0, 0, "Email address:");
		final TextBox uid = new TextBox();
		grid.setWidget(0, 1, uid);
		grid.setText(1, 0, "Password:");
		final PasswordTextBox password = new PasswordTextBox();
		grid.setWidget(1, 1, password);
		
		Button b = new Button("Login",
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
                    {
						Authentication auth = new Authentication(
								uid.getText(), password.getText());
						hide();
						Stellation2.login(auth);
                    }
				}
		);
		grid.getCellFormatter().setHorizontalAlignment(2, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(2, 1, b);
		
		Label label = new Label("Register");
		label.setStyleName("Link");
		label.addClickHandler(
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
                    {
						hide();
						new CreatePlayerDialog().show();
                    }
				}
		);
		grid.setWidget(2, 0, label);
		
		center();
    }
}
