/* A Group for use when all else fails.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/FallbackGroup.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.common.model.CObject;

public class FallbackGroup extends AbstractGroup<CObject>
{
	public FallbackGroup(CObject obj)
    {
		super(obj);
		setHeader("Unknown object #"+obj.getId());
    }
	
	@Override
	public void onChange(ChangeCallback cb)
	{
	}
}
