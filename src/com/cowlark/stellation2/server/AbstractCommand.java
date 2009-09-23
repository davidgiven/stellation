/* Logger helper class.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/AbstractCommand.java,v $
 * $Date: 2009/09/23 08:27:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server;

import com.cowlark.stellation2.common.Authentication;
import com.cowlark.stellation2.common.UpdateBatch;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.server.model.SPlayer;
import com.cowlark.stellation2.server.model.SUniverse;

public abstract class AbstractCommand
{
	public SPlayer getPlayer(Authentication auth)
		throws StellationException
	{
		SUniverse universe = SUniverse.getStaticUniverse();
		Log.log("running game");
		universe.timePassing();
		Log.log("end running game");
		return universe.authenticatePlayer(auth);
	}
	
	public UpdateBatch returnBatch(SPlayer player, long since)
	{
		UpdateBatch batch = new UpdateBatch();
		Log.log("updating visibility");
		player.updateVisibility();
		Log.log("done visibility search");
		player.addChangedItemsToBatch(batch, since);
		return batch;
	}	
}
