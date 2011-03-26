package com.cowlark.stellation3.common.game;

import java.util.HashMap;
import com.cowlark.stellation3.common.database.Hash;
import com.cowlark.stellation3.common.database.MessageListener;
import com.cowlark.stellation3.common.database.Reader;
import com.cowlark.stellation3.common.database.Transport;

public class Static
{
	public static class Key
	{
		public final Hash cid;
		public final Hash kid;
		
		public Key(Hash cid, Hash kid)
        {
			this.cid = cid;
			this.kid = kid;
        }
		
		@Override
		public int hashCode()
		{
			return cid.hashCode() * kid.hashCode();
		}
		
		@Override
		public boolean equals(Object other)
		{
			if (other instanceof Key)
			{
				Key op = (Key) other;
				return (op.cid == cid) && (op.kid == kid);
			}
			return false;
		}
	}
	
	private Transport _transport;
	private HashMap<Key, String> _data;
	
	public Static(Transport transport)
	{
		_transport = transport;
		_data = new HashMap<Key, String>();
	}
	
	public String getString(Hash cid, Hash kid)
	{
		return _data.get(new Key(cid, kid));
	}
	
	public double getDouble(Hash cid, Hash kid)
	{
		return Double.parseDouble(getString(cid, kid));
	}
	
	public int getInt(Hash cid, Hash kid)
	{
		return (int) Double.parseDouble(getString(cid, kid));
	}
	
	public void downloadData(final CompletionListener listener)
	{
		_transport.sendMessage(
				new MessageListener()
				{
					@Override
					public void onMessageReceived(Reader reader)
					{
						Hash h = reader.readHash();
						if (h != Hash.OK)
						{
							Game.Instance.protocolError();
							return;
						}
						
						while (!reader.isEOF())
						{
							Hash cid = reader.readHash();
							Hash kid = reader.readHash();
							String value = reader.readString();
							
							Key k = new Key(cid, kid);
							_data.put(k, value);
						}
						
						listener.onCompletion();
					}
				},
				"GetStatic");
	}
	
}
