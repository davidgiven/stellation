/* Logger helper class.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/RawCommand.java,v $
 * $Date: 2009/09/23 08:27:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server;

import java.io.IOException;
import com.cowlark.stellation2.common.exceptions.DatabaseCorruptException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.server.db.ServerDB;

abstract class RawCommand<T> extends AbstractCommand
{
	public abstract T run()
		throws StellationException;

	public T execute()
    		throws StellationException
    {
    	try
    	{
    		ServerDB.Instance.lock();		
    		try
    		{
    			return run();
    		}
    		finally
    		{
    			ServerDB.Instance.unlock();
    		}
    	}
    	catch (IOException e)
    	{
    		throw new DatabaseCorruptException(e);
    	}
    }
}