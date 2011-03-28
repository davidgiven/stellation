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
		mb.emitPlainText("Cargo: M=");
		mb.emitPlainText(S.CARGO_FORMAT.format(CargoM.get()));
		mb.emitPlainText(" A=");
		mb.emitPlainText(S.CARGO_FORMAT.format(CargoA.get()));
		mb.emitPlainText(" O=");
		mb.emitPlainText(S.CARGO_FORMAT.format(CargoO.get()));
		
		return mb.getMarkup();
	}
}
