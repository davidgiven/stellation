/* An attempt has been made to do something to an inaccessible object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/TugAlreadyLoadedException.java,v $
 * $Date: 2009/09/16 23:14:51 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class TugAlreadyLoadedException extends StellationException
{
    private static final long serialVersionUID = 4681432456955687490L;

	public TugAlreadyLoadedException(long id)
    {
		super(Long.toString(id));
    }
}
