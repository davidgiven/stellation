package com.cowlark.stellation3.common.markup;

import com.cowlark.stellation3.common.model.SObject;

public class MarkupBuilder implements HasMarkup
{
	private StringBuilder _sb;
	
	public MarkupBuilder()
	{
		clear();
	}
	
	public void clear()
	{
		_sb = new StringBuilder();
	}
	
	public String getMarkup()
	{
		return _sb.toString();
	}
	
	private void cmd(String... cmds)
	{
		_sb.append('\1');
		int i = 0;
		for (i = 0; i < cmds.length-1; i++)
		{
			_sb.append(cmds[i]);
			_sb.append('\2');
		}
		_sb.append(cmds[i]);
		_sb.append('\1');
	}
	
	@Override
	public void indent(int spaces)
	{
		cmd("indent", String.valueOf(spaces));
	}
	
	@Override
	public void emitPlainText(String text)
	{
		_sb.append(text);
	}
	
	@Override
	public void emitBoldText(String text)
	{
		cmd("bold", text);
	}
	
	@Override
	public void emitTime(long time)
	{
		cmd("time", String.valueOf(time));
	}
	
	@Override
	public void emitLink(String text, SObject object)
	{
		cmd("link", text, String.valueOf(object.Oid));
	}
}
