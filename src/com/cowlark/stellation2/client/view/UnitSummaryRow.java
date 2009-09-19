/* The middle pane unit summary DataRow.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/UnitSummaryRow.java,v $
 * $Date: 2009/09/19 12:06:09 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.monitors.UnitDamageMonitor;
import com.cowlark.stellation2.client.monitors.UnitNameMonitor;
import com.cowlark.stellation2.client.monitors.UnitNotesMonitor;
import com.cowlark.stellation2.client.ui.DataRow;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.Widget;

public class UnitSummaryRow extends DataRow implements Identifiable
{
	private DBRef<CUnit> _unit = DBRef.NULL();
	private Widget[] _row = new Widget[3];
	
	public UnitSummaryRow(CUnit unit)
	{
		_unit = new DBRef<CUnit>(unit);
		
		_row[0] = new UnitNameMonitor(unit);
		_row[1] = new UnitDamageMonitor(unit);
		_row[2] = new UnitNotesMonitor(unit);
	}	
	
	public long getId()
	{
	    return _unit.getId();
	}
	
	public Widget[] getData()
	{
	    return _row;
	}
	
	@Override
	public String getComparisonKey()
	{
	    return _unit.get().getProperties().getName();
	}
}
