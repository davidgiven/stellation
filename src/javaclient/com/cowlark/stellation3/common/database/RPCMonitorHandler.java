package com.cowlark.stellation3.common.database;

import com.google.gwt.event.shared.EventHandler;

public interface RPCMonitorHandler extends EventHandler
{
	public void onRPCBeginning();
	public void onRPCEnding();
}
