/* Client-side generic ship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CFactory.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.db.ListOfClientObjects;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;

public abstract class CFactory extends CUnit
{
	@Property
    private boolean _deployed;

    @Property
    private ListOfClientObjects _warehouse = new ListOfClientObjects();
	
	public CFactory()
    {
    }
	
	public boolean isDeployed()
			throws OutOfScopeException
    {
		checkOwner();
	    return _deployed;
    }
	
	public ListOfClientObjects getWarehouse()
			throws OutOfScopeException
    {
		checkOwner();
	    return _warehouse;
    }
}
