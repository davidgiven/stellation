package com.cowlark.stellation3.common.game;

import java.util.Vector;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneAspect;
import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.model.SObject;
import com.cowlark.stellation3.common.model.SStar;

public class StarMap
{
	private Pane _starmapPane;
	private Vector<Controller> _stars;
	
	public StarMap()
	{
		_stars = new Vector<Controller>();
		
		for (SObject o : Game.Instance.Galaxy.VisibleStars)
		{
			SStar star = (SStar) o;
			StarMapStarController.StarData sd = new
				StarMapStarController.StarData();
			sd.x = star.X.get();
			sd.y = star.Y.get();
			sd.brightness = star.Brightness.get();
			sd.name = star.Name.get();
			
			StarMapStarController smsc =
				Game.Instance.createStarMapStarController(null, sd);
			_stars.add(smsc);
		}
	}
	
	public void show()
	{
		_starmapPane = Game.Instance.showPane(PaneAspect.STARMAP, null);
		_starmapPane.updateControllers(_stars);
	}
}
