package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.controllers.ButtonsController;
import com.cowlark.stellation3.common.controllers.ButtonsHandler;
import com.cowlark.stellation3.gwt.ControllerImpl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;

public class ButtonsControllerImpl extends ControllerImpl
	implements ButtonsController
{
	private final PushButton[] _buttons;
	private final ButtonsHandler _bh;
	
	public ButtonsControllerImpl(ButtonsHandler bh, String[] strings)
    {
		super(2);
		
		_bh = bh;
		_buttons = new PushButton[strings.length];
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_JUSTIFY);
		
		for (int i = 0; i < strings.length; i++)
		{
			final int index = i;
			PushButton b = new PushButton(strings[i],
					new ClickHandler()
					{
						@Override
						public void onClick(ClickEvent event)
						{
							if (_bh != null)
								_bh.onButtonPressed(ButtonsControllerImpl.this, index);
						}
					});
			
//			b.setWidth("100%");
			_buttons[i] = b;
			hp.add(b);
		}
	
		setCell(1, hp);
    }
}
