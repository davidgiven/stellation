/* Server-side jumpship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SJumpship.java,v $
 * $Date: 2009/09/14 22:15:34 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.model.CJumpship;
import com.cowlark.stellation2.server.db.CClass;

@CClass(name = CJumpship.class)
public class SJumpship extends SShip
{
    private static final long serialVersionUID = -4568269796382604197L;
    
    public SJumpship initialise()
    {
        super.initialise();
        return this;
    }
    
	public Properties getProperties()
	{
		return PropertyStore.Jumpship;
	}

	@Override
	public void onAdditionTo(SObject parent)
	{
	    super.onAdditionTo(parent);
	    SFleet fleet = parent.toFleet();
	    if (fleet != null)
	    	fleet.setJumpshipCount(fleet.getJumpshipCount() + 1);
	}
	
	@Override
	public void onRemovalFrom(SObject parent)
	{
	    SFleet fleet = parent.toFleet();
	    if (fleet != null)
	    	fleet.setJumpshipCount(fleet.getJumpshipCount() - 1);
	    super.onRemovalFrom(parent);
	}
}
