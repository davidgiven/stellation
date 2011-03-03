package com.cowlark.stellation3.common.database;

import java.util.HashMap;
import com.google.gwt.user.client.Window;

public abstract class Transport
{
	private HashMap<Integer, MessageListener> _listeners =
		new HashMap<Integer, MessageListener>();
	private int _messageId = 0;
	
	private int allocateMessageId()
	{
		return _messageId++;
	}
	
	public void sendMessage(MessageListener listener, String... data)
	{
		int mid = allocateMessageId();
		_listeners.put(mid, listener);
		
		String[] packet = new String[data.length + 1];
		packet[0] = String.valueOf(mid);
		System.arraycopy(data, 0, packet, 1, data.length);
		
		sendRawMessage(packet);
	}
	
	protected void ioError(Throwable e)
	{
		Window.alert(e.toString());
	}
	
	protected void processRawMessage(Reader r)
	{
		int tag = r.readInt();
		MessageListener listener = _listeners.get(tag);
		_listeners.remove(tag);
		
		listener.onMessageReceived(r);
	}
	
	protected abstract void sendRawMessage(String[] data);
}
