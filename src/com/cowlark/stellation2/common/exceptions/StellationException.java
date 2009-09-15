/* Abstract exception superclass.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/StellationException.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.common.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class StellationException extends Exception
	implements IsSerializable
{
    private static final long serialVersionUID = -3296158011329835639L;

	public StellationException()
    {
	    super();
    }

	public StellationException(String arg0, Throwable arg1)
    {
	    super(arg0, arg1);
    }

	public StellationException(String arg0)
    {
	    super(arg0);
    }

	public StellationException(Throwable arg0)
    {
	    super(arg0);
    }
	
	public StellationException(long id)
	{
		super(Long.toString(id));
	}
}
