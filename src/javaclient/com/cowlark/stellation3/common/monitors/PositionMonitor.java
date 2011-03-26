package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.LabelController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SStar;
import com.cowlark.stellation3.gwt.S;

public class PositionMonitor extends Monitor<SStar>
{
	private LabelController _controller;
	
	public PositionMonitor(SStar o)
    {
		super(o);
		_controller = Game.Instance.createLabelController("Position");
    }
	
	@Override
	protected void update(SStar object)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		sb.append(S.COORD_FORMAT.format(object.X.get()));
		sb.append(", ");
		sb.append(S.COORD_FORMAT.format(object.Y.get()));
		sb.append("]");
		
		_controller.setStringValue(sb.toString());
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_controller);
	}
}
