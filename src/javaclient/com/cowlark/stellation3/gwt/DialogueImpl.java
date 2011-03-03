package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.controllers.ControllerGroup;
import com.cowlark.stellation3.common.controllers.Dialogue;
import com.cowlark.stellation3.common.controllers.DialogueHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

public class DialogueImpl extends DialogBox implements Dialogue
{
	private final ControllerGroup _cg;
	private final DialogueHandler _dh;
	
	public DialogueImpl(ControllerGroup cg, DialogueHandler dh)
    {
		_cg = cg;
		_dh = dh;
		
		setTitle(cg.getName());
		
		FlexTable ft = ControllerRenderer.createRendererContainer();
		add(ft);
		
		ControllerRenderer.renderGroup(ft, cg);
		
		center();
		show();
    }
	
	@Override
	public void cancelDialogue()
	{
		hide();
		if (_dh != null)
			_dh.onDialogueCancelled(this);
	}
	
	@Override
	public void closeDialogue()
	{
		hide();
		if (_dh != null)
			_dh.onDialogueClosed(this);
	}
}
