package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.controllers.StarMapStarHandler;

public class StarMapStarControllerImpl implements StarMapStarController
{
	private StarMapStarController.StarData _starData;
	
	public StarMapStarControllerImpl(StarMapStarHandler smsh,
			StarMapStarController.StarData sd)
    {
		setStarData(sd);
    }
	
	public StarData getStarData()
	{
		return _starData;
	}
	
	@Override
	public void setStarData(StarData sd)
	{
		_starData = sd;
	}
}
