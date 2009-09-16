/* Represents an individual row in a DataTable.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/SimpleDataRow.java,v $
 * $Date: 2009/09/16 23:14:51 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.Widget;

public class SimpleDataRow implements DataRow
{
	private Widget[] _data;
	
	public SimpleDataRow(Widget... data)
    {
		_data = data;
    }
	
	public Widget[] getData()
	{
		return _data;
	}
}
