
package com.cowlark.stellation3.gwt;

import com.cowlark.stellation3.common.database.Database;
import com.cowlark.stellation3.common.database.Reader;
import com.cowlark.stellation3.common.model.SObject;
import com.google.gwt.core.client.EntryPoint;

public class Stellation3 implements EntryPoint
{
	public void onModuleLoad()
	{
		String[] s = new String[] {
			"1", "Name", "String", "Test",
			"1", "Location", "Object", "0",
			"1", "Class", "Token", "SObject",
			"1", "X", "Number", "12.34"
		};
		
		Reader reader = new Reader(s);
		Database.loadBatch(reader);
		
		SObject o = Database.get(1);
	}
}
