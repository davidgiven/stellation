package com.cowlark.stellation3.common.monitors.summarypane;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.markup.MarkupBuilder;
import com.cowlark.stellation3.common.model.SStar;
import com.cowlark.stellation3.common.monitors.Monitor;

public class StarSummaryMonitor extends Monitor<SStar>
{
	private MarkupController _controller;
	
	public StarSummaryMonitor(SStar o)
    {
		super(o);
		_controller = Game.Instance.createMarkupController(null, null);
    }
	
	@Override
	protected void update(SStar star)
	{
		MarkupBuilder mb = new MarkupBuilder();
		mb.emitLink(star.Name.get(), star);
		_controller.setStringValue(mb.getMarkup());
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_controller);
	}
}
