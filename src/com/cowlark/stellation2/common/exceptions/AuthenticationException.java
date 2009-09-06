/* Authentication failed.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/AuthenticationException.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class AuthenticationException extends StellationException
{
    private static final long serialVersionUID = -7642199619185145470L;

    public AuthenticationException()
    {
    	super();
    }
    
	public AuthenticationException(String name)
    {
		super(name);
    }
}
