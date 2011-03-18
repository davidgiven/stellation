package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.markup.MarkupBuilder;
import com.cowlark.stellation3.common.model.SPlayer;

public class PlayerSummaryMonitor extends CompositeMonitor<SPlayer>
{
	private MarkupController _title;
	
	public PlayerSummaryMonitor(SPlayer o)
    {
	    super(o);
	    _title = Game.Instance.createMarkupController(null, null);
    }
	
	@Override
	protected void update(SPlayer object)
	{
		MarkupBuilder mb = new MarkupBuilder();
		mb.emitBoldText(object.Name.get());
		mb.emitPlainText(" of ");
		mb.emitBoldText(object.EmpireName.get());
		_title.setStringValue(mb.getMarkup());
		
		beginUpdate();
		endUpdate();
		updateAllMonitors();
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_title);
	    super.emitControllers(list);
	}
}
