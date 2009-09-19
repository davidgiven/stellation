/* Represents an individual row in a DataTable.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/DataRow.java,v $
 * $Date: 2009/09/19 12:06:09 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.ui;

import com.google.gwt.user.client.ui.Widget;

public abstract class DataRow implements Comparable<DataRow>
{
	abstract public Widget[] getData();
	
	public String getComparisonKey()
	{
		return "@" + hashCode();
	}
	
	public int compareTo(DataRow o)
	{
		return getComparisonKey().compareTo(o.getComparisonKey());
	}
}
