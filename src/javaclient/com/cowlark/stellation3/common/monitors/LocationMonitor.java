package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.LocationController;
import com.cowlark.stellation3.common.controllers.LocationHandler;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SObject;

public class LocationMonitor extends Monitor<SObject> implements
		LocationHandler
{
	private LocationController _controller;
	
	public LocationMonitor(SObject o)
    {
		super(o);
		_controller = Game.Instance.createLocationController(this, "Location");
    }
	
	@Override
	protected void update(SObject object)
	{
		_controller.setLocation(object.Location.get());
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_controller);
	}
}
