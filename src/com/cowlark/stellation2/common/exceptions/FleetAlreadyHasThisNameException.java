/* An attempt has been made to rename a fleet to a name that's already in use.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/FleetAlreadyHasThisNameException.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class FleetAlreadyHasThisNameException extends StellationException
{
    private static final long serialVersionUID = -4856359285196418841L;
    
	public FleetAlreadyHasThisNameException(String name)
    {
		super(name);
    }
}
