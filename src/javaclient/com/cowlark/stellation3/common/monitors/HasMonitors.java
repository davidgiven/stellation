package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;

public interface HasMonitors
{
	public void emitControllers(List<Controller> controllers);
	public void attach(HasMonitors parent);
	public void detach();
	public void updateAllMonitors();
}
