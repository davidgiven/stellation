package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.LabelController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SObject;

public class ObjectIdMonitor extends Monitor<SObject>
{
	private LabelController _controller;
	
	public ObjectIdMonitor(SObject o)
    {
		super(o);
		_controller = Game.Instance.createLabelController("Object ID");
    }
	
	@Override
	protected void update(SObject object)
	{
		_controller.setStringValue("#"+object.Oid);
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_controller);
	}
}
