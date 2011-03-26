package com.cowlark.stellation3.gwt.controllers;

import com.cowlark.stellation3.common.controllers.ObjectSummaryController;
import com.cowlark.stellation3.common.markup.MarkupBuilder;
import com.cowlark.stellation3.gwt.ui.MarkupLabelWidget;

public class ObjectSummaryControllerImpl extends ControllerImpl
	implements ObjectSummaryController
{
	private MarkupLabelWidget _nameWidget;
	private MarkupLabelWidget _damageWidget;
	private MarkupLabelWidget _detailWidget;
	
	private String _nameMarkup;
	private int _currentDamage;
	private int _maxDamage;
	private String _detailMarkup;
	
	public ObjectSummaryControllerImpl()
    {
		super(3);
		
		_nameWidget = new MarkupLabelWidget();
		_damageWidget = new MarkupLabelWidget();
		_detailWidget = new MarkupLabelWidget();
		
		setCell(0, _nameWidget);
		setCell(1, _damageWidget);
		setCell(2, _detailWidget);
		
		_nameMarkup = "";
		_currentDamage = 0;
		_maxDamage = 100;
		_detailMarkup = "";
    }
	
	@Override
	public void setNameMarkup(String markup)
	{
		_nameMarkup = markup;
		_nameWidget.setMarkup(markup);
	}
	
	@Override
	public void setDamage(int current, int max)
	{
		_currentDamage = current;
		_maxDamage = max;
		
		MarkupBuilder mb = new MarkupBuilder();
		mb.emitPlainText(String.valueOf(_currentDamage));
		mb.emitPlainText(" / ");
		mb.emitPlainText(String.valueOf(_maxDamage));
		_damageWidget.setMarkup(mb.getMarkup());
	}
	
	@Override
	public void setDetailsMarkup(String markup)
	{
		_detailMarkup = markup;
		_detailWidget.setMarkup(markup);
	}
}
