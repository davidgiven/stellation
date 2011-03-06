package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Pane;
import com.cowlark.stellation3.common.controllers.PaneHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;

public class DialogueImpl extends DialogBox implements Pane
{
	private final ControllerGroup _cg;
	private final PaneHandler _ph;
	
	public DialogueImpl(ControllerGroup cg, PaneHandler ph)
    {
		_cg = cg;
		_ph = ph;
		
		setTitle(cg.getName());
		
		FlexTable ft = ControllerRenderer.createRendererContainer();
		add(ft);
		
		ControllerRenderer.renderGroup(ft, cg);
		
		center();
		show();
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
