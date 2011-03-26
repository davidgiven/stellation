package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SFleet;
import com.cowlark.stellation3.common.model.SObject;
import com.cowlark.stellation3.common.model.SPlayer;
import com.cowlark.stellation3.common.model.SShip;

public class StarFleetMonitor extends CompositeMonitor<SFleet>
{
	private GroupTitleController _title;
	
	public StarFleetMonitor(SFleet o)
    {
		super(o);
		
	    _title = Game.Instance.createGroupTitleController(null);
    }
	
	@Override
	protected void update(SFleet object)
	{
		String title = object.Name.get();
		SPlayer owner = object.Owner.get();
		if (owner == Game.Instance.Player)
			title += " (yours)";
		else if (owner != null)
			title += " ("+owner.Name.get()+")";
		
		_title.setStringValue(title);
		
		for (SObject o : object.Contents)
		{
			if (o instanceof SShip)
			{
				SShip unit = (SShip) o;
				Monitor<?> m = unit.createSummaryMonitor();
				addMonitor(m);
			}
		}
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_title);
		super.emitControllers(list);
	}
}
