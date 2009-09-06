/* A View for use when all else fails.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/FallbackView.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.common.model.CObject;
import com.google.gwt.user.client.ui.Label;

public class FallbackView extends AbstractView<CObject>
{
	public FallbackView(CObject obj)
    {
		super(obj);
		initWidget(new Label("Unknown object #"+obj.getId()));
    }
	
	@Override
	public void onChange(ChangeCallback cb)
	{
	}
}
