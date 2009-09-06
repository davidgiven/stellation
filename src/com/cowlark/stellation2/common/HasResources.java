/* This object has resources.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/HasResources.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common;

import com.cowlark.stellation2.common.exceptions.OutOfScopeException;

public interface HasResources extends Identifiable
{
	public Resources getResources() throws OutOfScopeException;
}
