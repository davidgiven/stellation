package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface StarMapResources extends ClientBundle
{
	public static final StarMapResources Instance = GWT.create(StarMapResources.class);
	
	@Source("starmap-background.jpg")
	public ImageResource background();
	
	@Source("sphere.png")
	public ImageResource star();
}
