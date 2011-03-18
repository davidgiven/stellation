package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.controllers.StarMapStarHandler;
import com.google.gwt.user.client.ui.Image;

public class StarMapStarControllerImpl extends Image implements StarMapStarController
{
	private StarMapStarController.StarData _starData;
	private double _scale = 1.0;
	
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
		setStarImage(sd.brightness);
	}
	
	private void setStarImage(double b)
	{
		if (b < 2.0)
			setUrl("star1.png");
		else if (b < 3.0)
			setUrl("star2.png");
		else if (b < 4.0)
			setUrl("star3.png");
		else if (b < 5.0)
			setUrl("star4.png");
		else if (b < 6.0)
			setUrl("star5.png");
		else if (b < 7.0)
			setUrl("star6.png");
		else if (b < 8.0)
			setUrl("star7.png");
		else if (b < 9.0)
			setUrl("star8.png");
		else
			setUrl("star9.png");
	}
	
	public void setScale(double scale)
	{
		_scale = scale;
		setPixelSize((int)(64 * _scale),
				(int)(64 * _scale));
	}
	
	public double getOffsetX()
	{
		return getWidth() * 0.5;
	}
	
	public double getOffsetY()
	{
		return getHeight() * 0.5;
	}
}
