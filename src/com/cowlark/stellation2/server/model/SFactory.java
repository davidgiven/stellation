/* Server-side generc unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SFactory.java,v $
 * $Date: 2009/09/23 09:43:58 $
 * $Author: dtrg $
 * $Revision: 1.4 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.FactoryAlreadyBuildingException;
import com.cowlark.stellation2.common.exceptions.FactoryNotBuildingException;
import com.cowlark.stellation2.common.exceptions.InvalidObjectException;
import com.cowlark.stellation2.common.exceptions.StellationException;
import com.cowlark.stellation2.common.model.CFactory;
import com.cowlark.stellation2.server.db.CClass;
import com.cowlark.stellation2.server.db.ListOfServerIntegers;
import com.cowlark.stellation2.server.db.Property;

@CClass(name = CFactory.class)
public abstract class SFactory extends SUnit
{
    private static final long serialVersionUID = -7286755547474587404L;
    
    @Property(scope = S.OWNER)
    private ListOfServerIntegers _warehouse = new ListOfServerIntegers();
    
    @Property(scope = S.OWNER)
    private boolean _buildingContinuously = false;
    
    @Property(scope = S.OWNER)
    private int _nowBuilding = PropertyStore.NOTHING;
    
    @Property(scope = S.OWNER)
    private long _completionTime = -1;
    
	@Property
	private DBRef<STimer> _buildTimer = DBRef.NULL();
	
	@Override
	public SFactory initialise()
	{
	    super.initialise();
	    return this;
	}
	
	@Override
	public SFactory toFactory()
	{
		return this;
	}
	
	public ListOfServerIntegers getWarehouse()
    {
	    return _warehouse;
    }
	
	@Override
	public long timerExpiry(STimer timer)
	{
		if (timer.equals(_buildTimer))
		{
			Properties properties = PropertyStore.getProperties(_nowBuilding);
			if (properties.isStatic())
			{
				getStar().createObject(getOwner(), _nowBuilding);
				
				getStar().log(getOwner().getEmpire() + "'s " + getProperties().getName() +
						" has completed building " + properties.getName() +
						" and deployed it.");
			}
			else
			{
				_warehouse.add(_nowBuilding);
				
				getStar().log(getOwner().getEmpire() + "'s " + getProperties().getName() +
						" has completed building " + properties.getName() +
						" and placed it in its warehouse.");
			}
			
			_buildTimer = DBRef.NULL();
			_nowBuilding = PropertyStore.NOTHING;
			_completionTime = -1;
			dirty();
			
			return S.CANCELTIMER;
		}
		
	    return super.timerExpiry(timer);
	}
	public void buildCmd(int typeid)
		throws StellationException
	{
		if (_nowBuilding != PropertyStore.NOTHING)
			throw new FactoryAlreadyBuildingException(getId());
		
		Properties properties = PropertyStore.getProperties(typeid);
		getStar().consume(properties.getBuildCost());
		
		STimer timer = new STimer().
			initialise((long)(properties.getBuildTime() * S.TICK), this);
		_completionTime = timer.getExpiry();
		_buildTimer = new DBRef<STimer>(timer);
		_buildingContinuously = false;
		_nowBuilding = typeid;
		
		getStar().log(getOwner().getEmpire() + "'s " + getProperties().getName() +
				" has started building " + properties.getName() + ".");
		
		dirty();
	}
	
	public void abortCmd()
		throws StellationException
	{
		if (_nowBuilding == PropertyStore.NOTHING)
			throw new FactoryNotBuildingException(getId());
	
		getStar().log(getOwner().getEmpire() + "'s " + getProperties().getName() +
				" has stopped building " +
				PropertyStore.getProperties(_nowBuilding).getName() + ".");
		
		STimer timer = _buildTimer.get();
		if (timer != null)
			timer.cancel();
		_buildTimer = DBRef.NULL();
		_nowBuilding = PropertyStore.NOTHING;
		_completionTime = -1;
		
		dirty();
	}
	
	public void deployCmd(SFleet fleet)
		throws StellationException
	{
		if (fleet.getStar() != getStar())
			throw new InvalidObjectException(fleet.getId());
		
		SPlayer owner = getOwner();
		for (int i : _warehouse)
		{
			fleet.createObject(owner, i)
				.toUnit()
				.makeAlive();
			
			getStar().log(getOwner().getEmpire() + "'s " + getProperties().getName() +
					" has deployed " +
					PropertyStore.getProperties(i).getName() + " into " +
					fleet.getName() + ".");
		}
		
		_warehouse.clear();
		dirty();
	}
}
