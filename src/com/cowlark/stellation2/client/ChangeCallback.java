/* Notification that something in the database has changed.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ChangeCallback.java,v $
 * $Date: 2009/09/05 10:18:34 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client;


public interface ChangeCallback
{
	public void onChange(ChangeCallback cb);
}