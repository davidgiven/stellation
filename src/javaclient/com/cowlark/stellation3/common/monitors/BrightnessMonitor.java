package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.LabelController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SStar;
import com.cowlark.stellation3.gwt.S;

public class BrightnessMonitor extends Monitor<SStar>
{
	private LabelController _controller;
	
	public BrightnessMonitor(SStar o)
    {
		super(o);
		_controller = Game.Instance.createLabelController("Brightness");
    }
	
	@Override
	protected void update(SStar object)
	{
		String v = S.COORD_FORMAT.format(object.Brightness.get());
		_controller.setStringValue(v);
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_controller);
	}
}
