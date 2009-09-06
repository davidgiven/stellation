/* Something has gone badly wrong with the database.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/exceptions/DatabaseCorruptException.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.exceptions;

public class DatabaseCorruptException extends StellationException
{
    private static final long serialVersionUID = -3645489914601617280L;

    public DatabaseCorruptException(String reason)
    {
    	super(reason);
    }
    
    public DatabaseCorruptException(Throwable wrapped)
    {
    	super(wrapped);
    }
}
