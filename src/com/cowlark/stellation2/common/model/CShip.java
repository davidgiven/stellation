/* Client-side generic ship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CShip.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.common.model;


public abstract class CShip extends CUnit
{
	public CShip()
    {
    }
	
	public CStar getStar()
	{
		CFleet fleet = getParent().toFleet();
		if (fleet == null)
			return null;
		return fleet.getParent().toStar();
	}
}
