/* An attempt has been made to use resources that aren't there.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/ResourcesNotAvailableException.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class ResourcesNotAvailableException extends StellationException
{
    private static final long serialVersionUID = 6436940791969665607L;

	public ResourcesNotAvailableException()
    {
    	super();
    }
    
	public ResourcesNotAvailableException(String name)
    {
		super(name);
    }
}
