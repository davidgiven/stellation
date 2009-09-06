/* A generic View.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/AbstractView.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.view;

import com.cowlark.stellation2.client.ui.ObjectWatcherComposite;
import com.cowlark.stellation2.common.model.CObject;

public abstract class AbstractView<T extends CObject>
	extends ObjectWatcherComposite<T>
{
	public AbstractView(T obj)
    {
		super(obj);
    }
}
