package com.cowlark.stellation3.common.markup;

public class MarkupBuilder implements HasMarkup
{
	private StringBuilder _sb;
	
	public MarkupBuilder()
	{
		_sb = new StringBuilder();
	}
	
	public String getMarkup()
	{
		return _sb.toString();
	}
	
	@Override
	public void emitPlainText(String text)
	{
		_sb.append(text);
	}
	
	@Override
	public void emitBoldText(String text)
	{
		_sb.append("\1bold\2");
		_sb.append(text);
		_sb.append("\1");
	}
}
