package com.cowlark.stellation3.gwt.controllers;

import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.controllers.MarkupHandler;
import com.cowlark.stellation3.gwt.ui.MarkupLabelWidget;

public class MarkupControllerImpl extends ControllerImpl
	implements MarkupController
{
	private final MarkupLabelWidget _markupWidget;
	private final MarkupHandler _mh;
	private String _markup;
	
	public MarkupControllerImpl(MarkupHandler mh, String label)
    {
		super((label == null) ? 1 : 2);
		_mh = mh;
		
		_markupWidget = new MarkupLabelWidget();
		if (label == null)
			setCell(0, _markupWidget);
		else
		{
			setCell(0, label + ":");
			setCell(1, _markupWidget);
		}
    }
	
	@Override
	public String getStringValue()
	{
	    return _markup;
	}
	
	@Override
	public void setStringValue(String value)
	{
		_markup = value;
		_markupWidget.setMarkup(_markup);
	}
}
