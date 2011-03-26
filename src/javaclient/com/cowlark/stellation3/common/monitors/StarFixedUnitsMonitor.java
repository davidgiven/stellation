package com.cowlark.stellation3.common.monitors;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.game.Game;
import com.cowlark.stellation3.common.model.SFleet;
import com.cowlark.stellation3.common.model.SObject;
import com.cowlark.stellation3.common.model.SStar;

public class StarFixedUnitsMonitor extends CompositeMonitor<SStar>
{
	private GroupTitleController _title;
	private MarkupController _carbonaceousAsteroids;
	private MarkupController _metallicAsteroids;
	
	public StarFixedUnitsMonitor(SStar o)
    {
		super(o);
		
	    _title = Game.Instance.createGroupTitleController("General");
	    
	    _carbonaceousAsteroids = Game.Instance.createMarkupController(null,
	    		"Carbonaceous asteroids");
	    _metallicAsteroids = Game.Instance.createMarkupController(null,
	    		"Metallic asteroids");
    }
	
	@Override
	protected void update(SStar object)
	{
		_carbonaceousAsteroids.setStringValue(
				String.valueOf((int) object.AsteroidsC.get()));
		_metallicAsteroids.setStringValue(
				String.valueOf((int) object.AsteroidsM.get()));
		
		for (SObject o : object.Contents)
		{
			if (o instanceof SFleet)
			{
				SFleet fleet = (SFleet) o;
				addMonitor(new StarFleetMonitor(fleet));
			}
		}
	}
	
	@Override
	public void emitControllers(List<Controller> list)
	{
		list.add(_title);
		
		SStar star = getMonitoredObject();
		if (star.AsteroidsC.get() > 0)
			list.add(_carbonaceousAsteroids);
		if (star.AsteroidsM.get() > 0)
			list.add(_metallicAsteroids);
		super.emitControllers(list);
	}
}
