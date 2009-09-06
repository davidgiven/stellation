/* Client-side universe.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CUniverse.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.db.MapOfClientObjects;
import com.cowlark.stellation2.server.db.Property;

public class CUniverse extends CObject
{
	@Property
	private MapOfClientObjects _players = new MapOfClientObjects();
	
	@Property
	private DBRef<CGalaxy> _galaxy = new DBRef<CGalaxy>();
	
	public CUniverse()
    {
    }

	public MapOfClientObjects getPlayers()
    {
	    return _players;
    }
	
	public CGalaxy getGalaxy()
	{
		return _galaxy.get();
	}
}
