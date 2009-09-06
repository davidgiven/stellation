/* An attempt has been made to do something only an administrator can do.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/NotAdministratorException.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class NotAdministratorException extends StellationException
{
    private static final long serialVersionUID = 8288223406930090062L;

	public NotAdministratorException()
    {
    	super();
    }
    
	public NotAdministratorException(String name)
    {
		super(name);
    }
}
