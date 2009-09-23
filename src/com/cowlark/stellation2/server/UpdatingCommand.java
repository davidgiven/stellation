/* Logger helper class.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/UpdatingCommand.java,v $
 * $Date: 2009/09/23 08:27:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server;

import java.io.IOException;
import com.cowlark.stellation2.common.Authentication;
import com.cowlark.stellation2.common.UpdateBatch;
import com.cowlark.stellation2.common.exceptions.DatabaseCorruptException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.server.db.ServerDB;
import com.cowlark.stellation2.server.model.SPlayer;

abstract class UpdatingCommand extends AbstractCommand
{
	public abstract void run(SPlayer player)
		throws StellationException;

	public UpdateBatch execute(Authentication auth, long since)
    		throws StellationException
    {
    	try
    	{
    		ServerDB.Instance.lock();		
    		try
    		{
    			SPlayer player = getPlayer(auth);
    			try
    			{
	    			run(player);
	    			
	    			return returnBatch(player, since);
	    		}
	    		catch (StellationException e)
	    		{
	    			e.setBatch(returnBatch(player, since));
	    			throw e;
	    		}
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