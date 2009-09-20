/* Client-side generic ship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CFactory.java,v $
 * $Date: 2009/09/20 21:50:35 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.db.ListOfClientIntegers;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;

public abstract class CFactory extends CUnit
{
    @Property
    private ListOfClientIntegers _warehouse = new ListOfClientIntegers();
	    
    @Property
    private boolean _buildingContinuously = false;
    
    @Property
    private int _nowBuilding = PropertyStore.NOTHING;
    
    @Property
    private long _completionTime = -1;
    
	public CFactory()
    {
    }
	
	public ListOfClientIntegers getWarehouse()
			throws OutOfScopeException
    {
		checkOwner();
	    return _warehouse;
    }
	
	public boolean isBuildingContinuously()
		throws OutOfScopeException
    {
		checkOwner();
	    return _buildingContinuously;
    }
	
	public int getNowBuilding()
		throws OutOfScopeException
	{
		checkOwner();
	    return _nowBuilding;
    }
	
	public long getCompletionTime()
		throws OutOfScopeException
    {
		checkOwner();
	    return _completionTime;
    }
}
