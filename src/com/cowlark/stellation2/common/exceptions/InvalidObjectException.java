/* An attempt has been made to do something to an inaccessible object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/InvalidObjectException.java,v $
 * $Date: 2009/09/09 23:17:00 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class InvalidObjectException extends StellationException
{
    private static final long serialVersionUID = 2431948670008513556L;

	public InvalidObjectException(long id)
    {
		super(Long.toString(id));
    }
}
