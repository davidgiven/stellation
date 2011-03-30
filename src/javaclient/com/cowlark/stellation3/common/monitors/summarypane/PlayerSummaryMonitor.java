package com.cowlark.stellation3.common.monitors.summarypane;

import java.util.List;
import java.util.TreeSet;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.markup.MarkupBuilder;
import com.cowlark.stellation3.common.model.SFleet;
import com.cowlark.stellation3.common.model.SObject;
import com.cowlark.stellation3.common.model.SPlayer;
import com.cowlark.stellation3.common.model.SStar;
import com.cowlark.stellation3.common.monitors.CompositeMonitor;

public class PlayerSummaryMonitor extends CompositeMonitor<SPlayer>
{
	private GroupTitleController _title;
	private MarkupController _playerTitle;
	
	public PlayerSummaryMonitor(SPlayer o)
    {
	    super(o);
	    _title = Game.Instance.createGroupTitleController("Summary");
	    _playerTitle = Game.Instance.createMarkupController(null, null);
    }
	
	@Override
	protected void update(SPlayer player)
	{
		MarkupBuilder mb = new MarkupBuilder();
		mb.emitBoldText(player.Name.get());
		mb.emitPlainText(" of ");
		mb.emitBoldText(player.EmpireName.get());
		_playerTitle.setStringValue(mb.getMarkup());
		
		beginUpdate();
		
		TreeSet<SFleet> fleets = new TreeSet<SFleet>();
		TreeSet<SStar> stars = new TreeSet<SStar>();
		
		for (SObject fo : player.Fleets)
		{
			SFleet fleet = (SFleet) fo;
			fleets.add(fleet);
			stars.add(fleet.Location.<SStar>get());
		}
		
		for (SStar star : stars)
		{
			addMonitor(new StarSummaryMonitor(star));
			
			for (SFleet fleet : fleets)
			{
				if (fleet.Location.<SStar>get() == star)
					addMonitor(new FleetSummaryMonitor(fleet));
			}
		}
		
		endUpdate();
		updateAllMonitors();
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_title);
		list.add(_playerTitle);
	    super.emitControllers(list);
	}
}
