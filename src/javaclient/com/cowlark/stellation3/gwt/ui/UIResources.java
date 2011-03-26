package com.cowlark.stellation3.gwt.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface UIResources extends ClientBundle
{
	static UIResources Instance = GWT.create(UIResources.class);
	
	ImageResource close();
	ImageResource maximise();
	ImageResource minimise();
	ImageResource reload();
	
	ImageResource galaxy();
	
	ImageResource star1();
	ImageResource star2();
	ImageResource star3();
	ImageResource star4();
	ImageResource star5();
	ImageResource star6();
	ImageResource star7();
	ImageResource star8();
	ImageResource star9();
}
