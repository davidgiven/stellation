/* Represents an individual row in a DataTable.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/DataRow.java,v $
 * $Date: 2009/09/20 21:48:39 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.Widget;

public abstract class DataRow implements Comparable<DataRow>
{
	private boolean _hidden = false;
	
	abstract public Widget[] getData();
	
	public boolean isHidden()
    {
	    return _hidden;
    }
	
	public DataRow setHidden(boolean hidden)
    {
	    _hidden = hidden;
	    return this;
    }

	public String getComparisonKey()
	{
		return "@" + hashCode();
	}
	
	public int compareTo(DataRow o)
	{
		return getComparisonKey().compareTo(o.getComparisonKey());
	}
}
