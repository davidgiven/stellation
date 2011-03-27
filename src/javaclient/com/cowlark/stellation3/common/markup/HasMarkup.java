package com.cowlark.stellation3.common.markup;

import com.cowlark.stellation3.common.model.SObject;

public interface HasMarkup
{
	public void indent(int spaces);
	public void emitPlainText(String text);
	public void emitBoldText(String text);
	public void emitTime(long time);
	public void emitLink(String text, SObject object);
}
