/* A DataRow wrapped around a single Monitor.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/FullWidthWidgetRow.java,v $
 * $Date: 2009/09/14 22:22:32 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ui.DataRow;
import com.google.gwt.user.client.ui.Widget;

public class FullWidthWidgetRow implements DataRow
{
	private Widget[] _row = new Widget[1];
	
	public FullWidthWidgetRow(Widget widget)
	{
		_row[0] = widget;
	}	
	
	public Widget[] getData()
	{
	    return _row;
	}
}
