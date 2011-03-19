package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface UIImages extends ClientBundle
{
	static UIImages Instance = GWT.create(UIImages.class);
	
	@Source("close.png")
	ImageResource close();
	
	@Source("download.png")
	ImageResource maximise();
	
	@Source("download-reversed.png")
	ImageResource minimise();
}
