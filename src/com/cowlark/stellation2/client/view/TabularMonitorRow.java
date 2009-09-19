/* A DataRow wrapped around a single Monitor.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/TabularMonitorRow.java,v $
 * $Date: 2009/09/19 12:06:09 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.monitors.Monitor;
import com.cowlark.stellation2.client.ui.DataRow;
import com.cowlark.stellation2.common.Identifiable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TabularMonitorRow extends DataRow implements Identifiable
{
	private Monitor<?> _monitor;
	private Widget[] _row = new Widget[2];
	
	public TabularMonitorRow(Monitor<?> monitor)
	{
		_monitor = monitor;
		
		_row[0] = new Label(monitor.getLabel() + ":");
		_row[1] = monitor;
	}	
	
	public long getId()
	{
	    return _monitor.getId();
	}
	
	public Widget[] getData()
	{
	    return _row;
	}
	
	@Override
	public String getComparisonKey()
	{
	    return _monitor.getLabel();
	}
}
