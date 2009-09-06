/* Client-side tug.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CTug.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;

public class CTug extends CShip
{
	@Property
	private DBRef<CUnit> _cargo = new DBRef<CUnit>();
	
	public CTug()
    {
    }
	
	@Override
	public String getShortName()
	{
	    return "Tug";
	}
	
	@Override
	public String getDescription()
	{
	    return "Tugs are small, powerful craft used to tow otherwise " +
	    		"unpowered vessels and other artifacts.";
	}
	
	public CUnit getCargo() throws OutOfScopeException
    {
		checkOwner();
	    return _cargo.get();
    }
}
