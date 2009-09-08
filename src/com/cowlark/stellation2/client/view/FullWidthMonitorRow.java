/* A DataRow wrapped around a single Monitor.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/FullWidthMonitorRow.java,v $
 * $Date: 2009/09/08 23:01:28 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.monitors.Monitor;
import com.cowlark.stellation2.client.ui.DataRow;
import com.cowlark.stellation2.common.Identifiable;
import com.google.gwt.user.client.ui.Widget;

public class FullWidthMonitorRow implements DataRow, Identifiable
{
	private Monitor<?> _monitor;
	private Widget[] _row = new Widget[1];
	
	public FullWidthMonitorRow(Monitor<?> monitor)
	{
		_monitor = monitor;
		_row[0] = monitor;
	}	
	
	public long getId()
	{
	    return _monitor.getId();
	}
	
	public Widget[] getData()
	{
	    return _row;
	}
}
