/* An attempt has been made to do something to an inaccessible object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/FactoryAlreadyBuildingException.java,v $
 * $Date: 2009/09/20 21:50:35 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class FactoryAlreadyBuildingException extends StellationException
{
    private static final long serialVersionUID = 3819187172503484880L;

	public FactoryAlreadyBuildingException(long id)
    {
		super(Long.toString(id));
    }
}
