/* An attempt has been made to rename a fleet to a name that's already in use.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/Attic/AlreadyMothballedException.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;


public class AlreadyMothballedException extends StellationException
{
    private static final long serialVersionUID = -7044384171568536041L;

	public AlreadyMothballedException(long id)
    {
		super(id);
    }
}
