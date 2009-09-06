/* Client-side jumpship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CJumpship.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

public class CJumpship extends CShip
{
	public CJumpship()
    {
    }

	@Override
	public String getShortName()
	{
	    return "Jumpship";
	}
	
	@Override
	public String getDescription()
	{
		return "A jumpship is used to carry the spatial flaw used to " +
				"bootstrap wormholes. Its primary function is to carry fleets " +
				"of other ships through interstellar distances. The spatial " +
				"flaw can also be used to send instantaneous communications " +
				"from one jumpship to another, but only ones for which the " +
				"exact resonant frequency of the flaw is known.";
	}
}
