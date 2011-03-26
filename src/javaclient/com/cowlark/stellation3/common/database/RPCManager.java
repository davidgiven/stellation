package com.cowlark.stellation3.common.database;

import com.cowlark.stellation3.common.game.CompletionListener;
import com.cowlark.stellation3.common.game.Game;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class RPCManager
{
	private Transport _transport;
	private String _authkey;
	private double _serverTime;
	private HandlerManager _handler;
	
	public RPCManager(Transport transport)
    {
	    _transport = transport;
	    _handler = new HandlerManager(this);
    }
	
	public HandlerRegistration addRPCMonitor(RPCMonitorHandler handler)
	{
		return _handler.addHandler(RPCMonitorEvent.TYPE, handler);
	}
	
	public void authenticate(String email, String password,
			final AuthenticationListener listener)
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
								listener.onAuthenticationSucceeded(playeroid);
								break;
							}
							
							case AuthenticationFailure:
								listener.onAuthenticationFailed();
								break;
								
							default:
								Game.Instance.protocolError();
								break;
						}
					}
				},
				"Authenticate", email, password);
	}
	
	public void doInitialSync(final CompletionListener listener)
	{
		_serverTime = 0.0;
		postCommand(
				new RPCListener()
				{
					@Override
					public void onRPCResult(Hash token)
					{
						if (token != Hash.OK)
						{
							Game.Instance.protocolError();
							return;
						}
						
						listener.onCompletion();
					}
				}
				, "Ping");
	}
	
	public void postCommand(final RPCListener listener, String... command)
	{
		String[] s = new String[command.length + 3];
		System.arraycopy(command, 0, s, 3, command.length);
		s[0] = "GameOperation";
		s[1] = _authkey;
		s[2] = String.valueOf(_serverTime);
		
		RPCMonitorEvent outputevent = new RPCMonitorEvent(false);
		_handler.fireEvent(outputevent);
		
		_transport.sendMessage(
				new MessageListener()
				{
					@Override
					public void onMessageReceived(Reader reader)
					{
						Hash result = reader.readHash();
						_serverTime = reader.readDouble();
						Game.Instance.Database.loadBatch(reader);
						
						RPCMonitorEvent inputevent = new RPCMonitorEvent(true);
						_handler.fireEvent(inputevent);
						
						if (listener != null)
							listener.onRPCResult(result);
					}
				},
				s
		);
	}
}
