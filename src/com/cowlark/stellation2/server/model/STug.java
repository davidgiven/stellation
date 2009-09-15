/* Server-side tug.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/STug.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.model.CTug;
import com.cowlark.stellation2.server.db.CClass;

@CClass(name = CTug.class)
public class STug extends SShip
{
    private static final long serialVersionUID = -3512617834755579012L;
    
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
    
    private void updateMass()
    {
		double mass = getProperties().getMass();

		for (SObject o : this)
		{
			SUnit unit = o.toUnit();
			if (unit != null)
				mass += unit.getMass();
		}

	    setMass(mass);
	    dirty();
    }
    
    @Override
    public void onAdditionOf(SObject object)
    {
        super.onAdditionOf(object);
        updateMass();
    }   
    
    @Override
    public void onRemovalOf(SObject object)
    {
        super.onRemovalOf(object);
        updateMass();
    }
}
