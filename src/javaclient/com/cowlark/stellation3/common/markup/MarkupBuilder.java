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
	
	private void cmd(MarkupCommand c, String... cmds)
	{
		_sb.append('\1');
		_sb.append(c.toString());
		if (cmds.length > 0)
		{
			_sb.append('\2');
			int i = 0;
			for (i = 0; i < cmds.length-1; i++)
			{
				_sb.append(cmds[i]);
				_sb.append('\2');
			}
			_sb.append(cmds[i]);
		}
		_sb.append('\1');
	}
	
	@Override
	public void indent(int spaces)
	{
		cmd(MarkupCommand.Indent, String.valueOf(spaces));
	}
	
	@Override
	public void emitPlainText(String text)
	{
		_sb.append(text);
	}
	
	@Override
	public void emitBoldText(String text)
	{
		cmd(MarkupCommand.Bold, text);
	}
	
	@Override
	public void emitTime(long time)
	{
		cmd(MarkupCommand.Time, String.valueOf(time));
	}
	
	@Override
	public void emitLink(String text, SObject object)
	{
		cmd(MarkupCommand.Link, text, String.valueOf(object.Oid));
	}
	
	@Override
	public void emitResources(double m, double a, double o)
	{
		cmd(MarkupCommand.Resources,
				String.valueOf(m),
				String.valueOf(a),
				String.valueOf(o));
	}
}
