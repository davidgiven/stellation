package com.cowlark.stellation3.common.monitors.summarypane;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.markup.MarkupBuilder;
import com.cowlark.stellation3.common.model.SFleet;
import com.cowlark.stellation3.common.monitors.Monitor;

public class FleetSummaryMonitor extends Monitor<SFleet>
{
	private MarkupController _title;
	
	public FleetSummaryMonitor(SFleet o)
    {
	    super(o);
	    _title = Game.Instance.createMarkupController(null, null);
    }
	
	@Override
	protected void update(SFleet fleet)
	{
		MarkupBuilder mb = new MarkupBuilder();
		mb.indent(1);
		mb.emitFleet(fleet);
		_title.setStringValue(mb.getMarkup());
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_title);
	}
}
