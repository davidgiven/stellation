/* Client core module.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/Stellation2.java,v $
 * $Date: 2009/09/07 22:28:14 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.client;

import com.cowlark.stellation2.client.dialog.LoginDialog;
import com.cowlark.stellation2.client.view.GameView;
import com.cowlark.stellation2.common.Authentication;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.common.model.CPlayer;
import com.cowlark.stellation2.common.model.CStar;
import com.cowlark.stellation2.common.model.CUniverse;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Stellation2 implements EntryPoint
{
	public final static RPCServiceAsync Service = GWT.create(RPCService.class);
	
	private static long _playerId;
	private static GameView _gameView;
	private static long _timeDelta;
	
	private static Authentication _auth;
	
	public void onModuleLoad()
	{
		Database.Instance = new ClientDB();
		
		//RootPanel.get("loadingMessage").getElement().setInnerHTML("");
		
		String username = Cookies.getCookie("username");
		String password = Cookies.getCookie("password");
		if ((username != null) && (password != null))
		{
			Authentication auth = new Authentication(username, password);
			login(auth);
		}
		else
			authenticationFailure();
	}
	
	public static void alert(String s)
	{
		Window.alert(s);
	}
	
	public static void authenticationFailure()
	{
		new LoginDialog().show();
	}
	
	private static void showPlayingScreen()
	{
		CUniverse universe = getUniverse();
		CPlayer player = universe.getPlayers().get(_auth.getUid()).toPlayer();
		_playerId = player.getId();

		_gameView = new GameView();
		RootPanel.get("contentArea").add(_gameView);
	}
	
	private static void clear()
	{
		RootPanel.get("contentArea").clear();
		ClientDB.clear();
	}

	public static CUniverse getUniverse()
	{
		return (CUniverse) ClientDB.get(S.UNIVERSE);
	}
	
	public static CPlayer getPlayer()
    {
	    return (CPlayer) ClientDB.get(_playerId);
    }
	
	public static void login(Authentication auth)
	{
		_auth = auth;
		ClientDB.clear();
		
		ClientDB.addChangeCallback(S.UNIVERSE,
				new ChangeCallback()
				{
					public void onChange(ChangeCallback cb)
		            {
						Cookies.setCookie("username", _auth.getUid());
						Cookies.setCookie("password", _auth.getPassword());
						ClientDB.removeChangeCallback(S.UNIVERSE, cb);
						showPlayingScreen();
		            }
				}
		);
		
		ClientDB.fetchUpdates();
	}

	public static void logout()
	{
		_auth = null;
		Cookies.setCookie("username", null);
		Cookies.setCookie("password", null);
		clear();

		authenticationFailure();
	}
	
	public static Authentication getAuthentication()
	{
		return _auth;
	}
	
	public static void showStarViewer(CStar star)
	{
		_gameView.showStarViewer(star);
	}
	
	public static void showRightPaneView(Widget view)
	{
		_gameView.showRightPaneView(view);
	}
	
	public static long getTimeDelta()
    {
	    return _timeDelta;
    }
	
	public static void setTimeDelta(long delta)
    {
	    _timeDelta = delta;
    }
}
