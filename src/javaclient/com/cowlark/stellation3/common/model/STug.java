package com.cowlark.stellation3.common.model;

import java.util.Iterator;
import com.cowlark.stellation3.common.markup.MarkupBuilder;

public class STug extends SShip
{
	public STug(int oid)
	{
		super(oid);
	}
	
	private SUnit getContents()
	{
		Iterator<SObject> i = Contents.iterator();
		if (i.hasNext())
			return (SUnit) i.next();
		return null;
	}
	
	@Override
	public String getSummaryDetailsMarkup()
	{
		SUnit contents = getContents();
		
		MarkupBuilder mb = new MarkupBuilder();
		mb.emitPlainText("Carrying: ");
		if (contents == null)
			mb.emitPlainText("nothing");
		else
			mb.emitPlainText(contents.getName());
		
		return mb.getMarkup();
	}
}
