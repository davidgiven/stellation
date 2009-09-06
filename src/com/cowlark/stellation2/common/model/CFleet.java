/* Client-side fleet.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CFleet.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;

public class CFleet extends CObject
{
	@Property
	private String _name;
	
	@Property
	private int _jumpshipCount;
	
	public CFleet()
    {
    }
	
	public String getName() throws OutOfScopeException
    {
		checkNear();
	    return _name;
    }
	
	public final int getJumpshipCount() throws OutOfScopeException
    {
		checkNear();
    	return _jumpshipCount;
    }

	public CFleet toFleet()
	{
		return this;
	}
}
