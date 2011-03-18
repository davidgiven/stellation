package com.cowlark.stellation3.gwt.ui;

import com.cowlark.stellation3.common.controllers.MarkupController;
import com.cowlark.stellation3.common.controllers.MarkupHandler;
import com.cowlark.stellation3.common.markup.HasMarkup;
import com.cowlark.stellation3.common.markup.MarkupParser;
import com.cowlark.stellation3.gwt.ControllerImpl;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class MarkupControllerImpl extends ControllerImpl
	implements MarkupController, HasMarkup
{
	private final Label _label;
	private final HTML _html;
	private final MarkupHandler _mh;
	private String _markup;
	private StringBuilder _rendering;
	
	public MarkupControllerImpl(MarkupHandler mh, String label)
    {
		super((label == null) ? 1 : 2);
		_mh = mh;
		
		_html = new HTML();
		if (label == null)
		{
			_label = null;
			setCell(0, _html);
		}
		else
		{
			_label = new Label(label);
			setCell(0, _label);
			setCell(1, _html);
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
		_rendering = new StringBuilder();
		MarkupParser.render(_markup, this);
		_html.setHTML(_rendering.toString());
	}
	
	@Override
	public void emitPlainText(String text)
	{
		for (int i=0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			switch (c)
			{
				case '<':
					_rendering.append("&lt;");
					break;
					
				case '>':
					_rendering.append("&gt;");
					break;
					
				case '&':
					_rendering.append("&amp;");
					break;
					
				default:
					_rendering.append(c);
			}
		}
	}
	
	@Override
	public void emitBoldText(String text)
	{
		_rendering.append("<b>");
		emitPlainText(text);
		_rendering.append("</b>");
	}
}
