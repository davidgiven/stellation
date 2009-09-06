/* Server-side tug.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/STug.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.model.CTug;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CTug.class)
public class STug extends SShip
{
    private static final long serialVersionUID = -3512617834755579012L;
    
	private static double REST_MASS = 1000.0;
    private static Resources BUILD_COST = new Resources(8000.0, 1000.0, 3000.0);
    private static double BUILD_TIME = 2.0;
    private static Resources MAINTENANCE_COST = new Resources(4.0, 1.0, 0.0);
    private static double MAX_DAMAGE = 100.0;

    @Property(scope = S.LOCAL)
    private DBRef<SUnit> _cargo = new DBRef<SUnit>();
    
    @Override
    public STug initialise()
    {
        super.initialise();
        return this;
    }
    
	@Override public double getRestMass() { return REST_MASS; }
	@Override public Resources getBuildCost() { return BUILD_COST; }
	@Override public double getBuildTime() { return BUILD_TIME; }
	@Override public Resources getMaintenanceCost() { return MAINTENANCE_COST; }
	@Override public double getMaxDamage() { return MAX_DAMAGE; }
	
	@Override
    public double getMass()
    {
		double mass = super.getMass();
		
		if (_cargo != null)
			mass += _cargo.get().getMass();
		
	    return mass;
    }
}
