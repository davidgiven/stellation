/* A generic Group.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/AbstractGroup.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ui.ObjectWatcherDataGroup;
import com.cowlark.stellation2.common.model.CObject;

public abstract class AbstractGroup<T extends CObject>
	extends ObjectWatcherDataGroup<T>
{
	public AbstractGroup(T obj)
    {
		super(obj);
    }
}
