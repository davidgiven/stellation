package com.cowlark.stellation3.gwt.ui;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.cowlark.stellation3.gwt.ControllerRenderer;
import com.google.gwt.user.client.ui.DialogBox;

public class DialogueImpl extends DialogBox implements Pane
{
	private final ControllerRenderer _renderer;
	private final PaneHandler _ph;
	private final String _title;
	
	public DialogueImpl(PaneHandler ph, String title)
    {
		_renderer = new ControllerRenderer();
		_ph = ph;
		_title = title;
		
		add(_renderer.getContainer());
		setText(_title);
		
		center();
		show();
    }
	
	@Override
	public void updateControllers(List<Controller> controllers)
	{
		_renderer.update(controllers);
		center();
	}
	
	@Override
	public void cancelPane()
	{
		hide();
		if (_ph != null)
			_ph.onPaneCancelled(this);
	}
	
	@Override
	public void closePane()
	{
		hide();
		if (_ph != null)
			_ph.onPaneClosed(this);
	}
}
