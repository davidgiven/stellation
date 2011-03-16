package com.cowlark.stellation3.common.controllers;

import java.util.List;

public interface Pane
{
	public void updateControllers(List<Controller> controllers);
	
	public void closePane();
	public void cancelPane();
}
