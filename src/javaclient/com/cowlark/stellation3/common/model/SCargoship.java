package com.cowlark.stellation3.common.model;

import com.cowlark.stellation3.common.markup.MarkupBuilder;
import com.cowlark.stellation3.gwt.S;

public class SCargoship extends SShip
{
	public SCargoship(int oid)
	{
		super(oid);
	}
	
	@Override
	public String getSummaryDetailsMarkup()
	{
		MarkupBuilder mb = new MarkupBuilder();
		mb.emitPlainText("Cargo: ");
		mb.emitResources(CargoM.get(), CargoA.get(), CargoO.get());
		return mb.getMarkup();
	}
}
