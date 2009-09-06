/* A Row for use when all else fails.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/FallbackRow.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ui.DataRow;
import com.cowlark.stellation2.common.model.CObject;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FallbackRow implements DataRow
{
	private Widget[] _data = new Widget[1];
	
	public FallbackRow(CObject obj)
    {
		_data[0] = new Label("Unknown object #"+obj.getId());
    }

	public Widget[] getData()
	{
	    return _data;
	}
}
