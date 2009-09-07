/* The middle pane unit summary DataRow.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/UnitSummaryRow.java,v $
 * $Date: 2009/09/07 22:28:13 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.monitors.UnitDamageMonitor;
import com.cowlark.stellation2.client.monitors.UnitNameMonitor;
import com.cowlark.stellation2.client.monitors.UnitNotesMonitor;
import com.cowlark.stellation2.client.ui.DataRow;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.Widget;

public class UnitSummaryRow implements DataRow, Identifiable
{
	private long _id;
	private Widget[] _row = new Widget[3];
	
	public UnitSummaryRow(CUnit unit)
	{
		_id = unit.getId();
		
		_row[0] = new UnitNameMonitor(unit);
		_row[1] = new UnitDamageMonitor(unit);
		_row[2] = new UnitNotesMonitor(unit);
	}	
	
	public long getId()
	{
	    return _id;
	}
	
	public Widget[] getData()
	{
	    return _row;
	}
}
