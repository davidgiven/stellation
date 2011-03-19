package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.game.CompletionListener;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class UIResources
{
	public static ImageElement Background;
	public static ImageElement[] Star;
	
	private static int _loaded;
	private static CompletionListener _listener;
	
	private static ImageResource[] _resources =
	{
		UIImages.Instance.galaxy(),
		UIImages.Instance.star1(),
		UIImages.Instance.star2(),
		UIImages.Instance.star3(),
		UIImages.Instance.star4(),
		UIImages.Instance.star5(),
		UIImages.Instance.star6(),
		UIImages.Instance.star7(),
		UIImages.Instance.star8(),
		UIImages.Instance.star9()
	};
	
	static
	{
		final Image[] images = new Image[_resources.length];
		
		_loaded = 0;
		LoadHandler handler = new LoadHandler()
		{
			@Override
			public void onLoad(LoadEvent event)
			{
				_loaded++;
				
				if (_loaded == _resources.length)
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
		
		for (int i=0; i<_resources.length; i++)
		{
			Image image = new Image();
			image.addLoadHandler(handler);
			image.setUrl(_resources[i].getURL());
			image.setVisible(false);
			RootPanel.get().add(image);
			images[i] = image;
		}
	}
	
	public static void waitForLoad(CompletionListener listener)
	{
		if (_loaded == _resources.length)
			listener.onCompletion();
		_listener = listener;
	}
}
