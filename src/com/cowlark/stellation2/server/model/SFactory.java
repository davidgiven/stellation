/* Server-side generc unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SFactory.java,v $
 * $Date: 2009/09/16 23:14:51 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.model.CFactory;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.ListOfServerObjects;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CFactory.class)
public abstract class SFactory extends SUnit
{
    private static final long serialVersionUID = -7286755547474587404L;
    
    @Property(scope = S.OWNER)
    private ListOfServerObjects<SUnit> _warehouse = new ListOfServerObjects<SUnit>();
    
	@Property
	private DBRef<STimer> _buildTimer = DBRef.NULL();
	
	@Override
	public SFactory initialise()
	{
	    super.initialise();
	    return this;
	}
	
	public ListOfServerObjects<SUnit> getWarehouse()
    {
	    return _warehouse;
    }
}
