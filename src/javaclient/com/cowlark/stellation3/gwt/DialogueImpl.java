package com.cowlark.stellation3.gwt;

import java.util.List;
import com.cowlark.stellation3.common.controllers.Controller;
import com.cowlark.stellation3.common.controllers.GroupTitleController;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.google.gwt.user.client.ui.DialogBox;

public class DialogueImpl extends DialogBox implements Pane
{
	private final ControllerRenderer _renderer;
	private final PaneHandler _ph;
	
	public DialogueImpl(PaneHandler ph)
    {
		_renderer = new ControllerRenderer();
		_ph = ph;
		
		add(_renderer.getContainer());
		
		center();
		show();
    }
	
	@Override
	public void updateControllers(List<Controller> controllers)
	{
		if (!controllers.isEmpty())
		{
			Controller c = controllers.get(0);
			if (c instanceof GroupTitleController)
			{
				GroupTitleController gtc = (GroupTitleController) c;
				setText(gtc.getTitle());
				controllers = controllers.subList(1, controllers.size());
			}
		}
		
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
