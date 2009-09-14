/* Server-side tug.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/STug.java,v $
 * $Date: 2009/09/14 22:15:34 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.model.CTug;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CTug.class)
public class STug extends SShip
{
    private static final long serialVersionUID = -3512617834755579012L;
    
    @Property(scope = S.LOCAL)
    private DBRef<SUnit> _cargo = new DBRef<SUnit>();
    
    @Override
    public STug initialise()
    {
        super.initialise();
        return this;
    }
    
    @Override
    public Properties getProperties()
    {
        return PropertyStore.Tug;
    }
    
	@Override
    public double getMass()
    {
		double mass = super.getMass();
		
		if (_cargo != null)
			mass += _cargo.get().getMass();
		
	    return mass;
    }
}
