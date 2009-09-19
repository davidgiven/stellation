/* A Row for use when all else fails.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/FallbackRow.java,v $
 * $Date: 2009/09/19 12:06:09 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ui.DataRow;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.model.CObject;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FallbackRow extends DataRow
{
	private DBRef<CObject> _object;
	private Widget[] _data = new Widget[1];
	
	public FallbackRow(CObject obj)
    {
		_object = new DBRef<CObject>(obj);
		_data[0] = new Label("Unknown object #"+obj.getId());
    }

	public Widget[] getData()
	{
	    return _data;
	}
	
	@Override
	public String getComparisonKey()
	{
	    return "#" + _object.getId();
	}
}
