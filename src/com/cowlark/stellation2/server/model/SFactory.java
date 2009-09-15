/* Server-side generc unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SFactory.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.AlreadyDeployedException;
import com.cowlark.stellation2.common.exceptions.AlreadyMothballedException;
import com.cowlark.stellation2.common.exceptions.MustBeInStarSystemException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.common.model.CFactory;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.ListOfServerObjects;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CFactory.class)
public abstract class SFactory extends SUnit
{
    private static final long serialVersionUID = -7286755547474587404L;
    
    @Property(scope = S.OWNER)
    private boolean _deployed;

    @Property(scope = S.OWNER)
    private ListOfServerObjects<SUnit> _warehouse = new ListOfServerObjects<SUnit>();
    
	@Property
	private DBRef<STimer> _buildTimer = DBRef.NULL();
	
	@Override
	public SFactory initialise()
	{
	    super.initialise();
	    _deployed = false;
	    return this;
	}
	
	public boolean isDeployed()
    {
	    return _deployed;
    }
	
	public ListOfServerObjects<SUnit> getWarehouse()
    {
	    return _warehouse;
    }
	
	public void deploy()
			throws StellationException
	{
		if (getLocation().toStar() == null)
			throw new MustBeInStarSystemException(getId());
		if (_deployed)
			throw new AlreadyDeployedException(getId());
		
		_deployed = true;
		dirty();
	}
	
	public void mothball()
			throws StellationException
	{
		if (getLocation().toStar() == null)
			throw new MustBeInStarSystemException(getId());
		if (!_deployed)
			throw new AlreadyMothballedException(getId());
		
		_deployed = false;
		dirty();
	}
}
