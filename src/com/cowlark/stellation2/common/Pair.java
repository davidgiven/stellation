/* A pair of two data objects.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/Pair.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common;

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Pair<T> implements IsSerializable, Serializable
{
    private static final long serialVersionUID = -7010140571913045695L;
    
	public T x;
	public T y;
}
