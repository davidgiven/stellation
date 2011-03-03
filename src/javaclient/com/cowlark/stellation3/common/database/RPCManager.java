package com.cowlark.stellation3.common.database;

import com.cowlark.stellation3.common.game.Game;

public class RPCManager
{
	private Transport _transport;
	private String _authkey;
	private double _serverTime;
	
	public RPCManager(Transport transport)
    {
	    _transport = transport;
    }
	
	public void authenticate(String email, String password)
	{
		assert(_authkey == null);
		
		_transport.sendMessage(
				new MessageListener()
				{
					@Override
					public void onMessageReceived(Reader reader)
					{
						switch(reader.readHash())
						{
							case OK:
							{
								_authkey = reader.readString();
								int playeroid = reader.readInt();
								Game.Instance.loggedIn(playeroid);
								break;
							}
							
							case AuthenticationFailure:
								Game.Instance.authenticationFailed();
								break;
								
							default:
								Game.Instance.protocolError();
								break;
						}
					}
				},
				"Authenticate", email, password);
	}
	
	public void doInitialSync()
	{
		_serverTime = 0.0;
		_transport.sendMessage(
				new MessageListener()
				{
					@Override
					public void onMessageReceived(Reader reader)
					{
						if (reader.readHash() != Hash.OK)
						{
							Game.Instance.protocolError();
							return;
						}
				
						_serverTime = reader.readDouble();
						Game.Instance.Database.loadBatch(reader);
						Game.Instance.initialSyncComplete();
					}
				},
				"GameOperation", _authkey, String.valueOf(_serverTime),
				"Ping"
		);
	}
}
