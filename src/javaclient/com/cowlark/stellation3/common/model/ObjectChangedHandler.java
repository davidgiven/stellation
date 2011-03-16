package com.cowlark.stellation3.common.model;

import com.google.gwt.event.shared.EventHandler;

public interface ObjectChangedHandler extends EventHandler
{
	public void onObjectChanged(SObject object);
}
