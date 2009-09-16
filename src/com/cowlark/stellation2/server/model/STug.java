/* Server-side tug.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/STug.java,v $
 * $Date: 2009/09/16 23:14:51 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.server.model;

import java.util.Iterator;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.exceptions.CargoCannotBeLoadedOntoTugException;
import com.cowlark.stellation2.common.exceptions.InvalidObjectException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.common.exceptions.TugAlreadyLoadedException;
import com.cowlark.stellation2.common.exceptions.TugNotLoadedException;
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
    public STug toTug()
    {
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
    
	public SUnit getCargo()
	{
		Iterator<SObject> i = iterator();
		if (i.hasNext())
			return (SUnit) i.next();
		return null;
	}

	public void loadCmd(SUnit cargo)
			throws StellationException
	{
		if (getCargo() != null)
			throw new TugAlreadyLoadedException(getId());
		SStar star = getStar();
		if (cargo.getLocation() != star)
			throw new InvalidObjectException(cargo.getId());
		if (!cargo.allowLoadingOntoTug())
			throw new CargoCannotBeLoadedOntoTugException(cargo.getId());
		
		cargo.makeDead();
		cargo.removeFromParent();
		add(cargo);
		
    	star.log(getOwner().getName() + "'s tug loads " +
    			cargo.getProperties().getName() + ".");
    	
		star.dirty();
		dirty();
	}
	
    public void unloadCmd()
    		throws StellationException
    {
    	SUnit cargo = getCargo();
    	if (cargo == null)
    		throw new TugNotLoadedException(getId());
    	SStar star = getStar();
    	
    	remove(cargo);
    	star.add(cargo);
    	cargo.makeAlive();
    	
    	star.log(getOwner().getName() + "'s tug unloads " +
    			cargo.getProperties().getName() + ".");
    	
    	star.dirty();
    	dirty();
    }
}
