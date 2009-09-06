/* Client-side tug.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CTug.java,v $
 * $Date: 2009/09/06 22:15:12 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
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
	public Properties getProperties()
	{
	    return PropertyStore.Tug;
	}
	
	public CUnit getCargo() throws OutOfScopeException
    {
		checkOwner();
	    return _cargo.get();
    }
}
