/* Callback interface for SyncableObjectList.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/SyncableObjectManager.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common;

public interface SyncableObjectManager<T extends Identifiable>
{
	public T create(long id);
	public void destroy(T object);
}
