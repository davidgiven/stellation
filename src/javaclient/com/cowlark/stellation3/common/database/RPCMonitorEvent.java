package com.cowlark.stellation3.common.database;

import com.google.gwt.event.shared.GwtEvent;

public class RPCMonitorEvent extends GwtEvent<RPCMonitorHandler>
{
	public static Type<RPCMonitorHandler> TYPE =
		new Type<RPCMonitorHandler>();
	
	private boolean _input;
	
	public RPCMonitorEvent(boolean input)
	{
		_input = input;
	}
	
	@Override
	public Type<RPCMonitorHandler> getAssociatedType()
	{
	    return TYPE;
	}
	
	@Override
	protected void dispatch(RPCMonitorHandler handler)
	{
		if (_input)
			handler.onRPCEnding();
		else
			handler.onRPCBeginning();
	}
}
