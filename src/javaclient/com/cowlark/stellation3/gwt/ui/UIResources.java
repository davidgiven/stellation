package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.game.CompletionListener;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class UIResources
{
	public static ImageElement Background;
	public static ImageElement[] Star;
	
	private static int _loaded;
	private static CompletionListener _listener;
	
	private static String[] _urls =
	{
		"galaxy.jpg",
		"star1.png",
		"star2.png",
		"star3.png",
		"star4.png",
		"star5.png",
		"star6.png",
		"star7.png",
		"star8.png",
		"star9.png"
	};
	
	static
	{
		final Image[] images = new Image[_urls.length];
		
		_loaded = 0;
		LoadHandler handler = new LoadHandler()
		{
			@Override
			public void onLoad(LoadEvent event)
			{
				_loaded++;
				
				if (_loaded == _urls.length)
				{
					Background = ImageElement.as(images[0].getElement());
					
					Star = new ImageElement[9];
					for (int i=0; i<9; i++)
						Star[i] = ImageElement.as(images[1+i].getElement());
					
					if (_listener != null)
						_listener.onCompletion();
					
					for (int i=0; i<images.length; i++)
						images[i].removeFromParent();
				}
			}
		};
		
		for (int i=0; i<_urls.length; i++)
		{
			Image image = new Image();
			image.addLoadHandler(handler);
			image.setUrl(_urls[i]);
			image.setVisible(false);
			RootPanel.get().add(image);
			images[i] = image;
		}
	}
	
	public static void waitForLoad(CompletionListener listener)
	{
		if (_loaded == _urls.length)
			listener.onCompletion();
		_listener = listener;
	}
}
