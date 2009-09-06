/* An attempt has been made to do something to an inaccessible object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/OutOfScopeException.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class OutOfScopeException extends StellationException
{
    private static final long serialVersionUID = 3367651251649798745L;

	public OutOfScopeException(long id)
    {
		super(Long.toString(id));
    }
}
