/* An attempt has been made to do something to an inaccessible object.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/FactoryNotBuildingException.java,v $
 * $Date: 2009/09/20 21:50:35 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class FactoryNotBuildingException extends StellationException
{
    private static final long serialVersionUID = 1134241870373602585L;

	public FactoryNotBuildingException(long id)
    {
		super(Long.toString(id));
    }
}
