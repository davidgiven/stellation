package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.controllers.StarMapStarController;
import com.cowlark.stellation3.common.controllers.StarMapStarHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class StarMapStarControllerImpl extends Image implements StarMapStarController
{
	private StarMapStarController.StarData _starData;
	private ImageResource _image;
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
		_image = StarMapResources.Instance.star();
		setUrl(_image.getURL());
	}
	
	public void setScale(double scale)
	{
		_scale = scale;
		setPixelSize((int)(_image.getWidth() * _scale),
				(int)(_image.getHeight() * _scale));
	}
	
	public double getOffsetX()
	{
		return _image.getWidth() * _scale * 0.5;
	}
	
	public double getOffsetY()
	{
		return _image.getHeight() * _scale * 0.5;
	}
}
