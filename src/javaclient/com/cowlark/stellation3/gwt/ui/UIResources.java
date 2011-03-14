package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.game.CompletionListener;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.widgetideas.graphics.client.ImageLoader;

public class UIResources
{
	public static ImageElement Background;
	public static ImageElement[] Star;
	
	private static boolean _loaded;
	private static CompletionListener _listener;
	
	private static String[] _urls =
	{
		"../galaxy.jpg",
		"../star1.png",
		"../star2.png",
		"../star3.png",
		"../star4.png",
		"../star5.png",
		"../star6.png",
		"../star7.png",
		"../star8.png",
		"../star9.png"
	};
	
	static
	{
		ImageLoader.loadImages(_urls,
				new ImageLoader.CallBack()
				{
					@Override
					public void onImagesLoaded(ImageElement[] imageElements)
					{
						Background = imageElements[0];
						
						Star = new ImageElement[9];
						System.arraycopy(imageElements, 1, Star, 0, 9);
						
						_loaded = true;
						if (_listener != null)
							_listener.onCompletion();
					}
				}
		);
	}
	
	public static void waitForLoad(CompletionListener listener)
	{
		if (_loaded)
			listener.onCompletion();
		_listener = listener;
	}
}
