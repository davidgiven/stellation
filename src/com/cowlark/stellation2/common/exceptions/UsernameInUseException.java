/* An attempt has been made to create a user whose uid is already in use.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/UsernameInUseException.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class UsernameInUseException extends StellationException
{
    private static final long serialVersionUID = 7324868009212429098L;

	public UsernameInUseException(String name)
    {
		super(name);
    }
}
