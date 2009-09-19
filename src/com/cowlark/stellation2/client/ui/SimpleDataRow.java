/* Represents an individual row in a DataTable.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/SimpleDataRow.java,v $
 * $Date: 2009/09/19 12:06:09 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.Widget;

public class SimpleDataRow extends DataRow
{
	private String _comparisonkey;
	private Widget[] _data;
	
	public SimpleDataRow(String comparisonkey, Widget... data)
    {
		_data = data;
    }
	
	public Widget[] getData()
	{
		return _data;
	}
	
	@Override
	public String getComparisonKey()
	{
	    return _comparisonkey;
	}
}
