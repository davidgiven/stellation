/* Timer obejct.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/STimer.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.S;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.server.db.Property;
import com.cowlark.stellation2.server.db.RootObject;

public class STimer extends RootObject
{
    private static final long serialVersionUID = -7615714529354772385L;

	@Property
	long _expiry;
	
	@Property
	DBRef<SObject> _object = DBRef.NULL();

	public STimer()
	{
	}
	
	public STimer initialise(long delta, SObject object)
    {
    	super.initialise(SUniverse.getStaticUniverse().getNextId());
		
		_expiry = System.currentTimeMillis() + delta;
		_object = new DBRef<SObject>(object);
		SUniverse.getStaticUniverse().addTimer(this);
		return this;
    }
	
	public long getExpiry()
    {
	    return _expiry;
    }
	
	public boolean equals(DBRef<STimer> o)
	{
		return getId() == o.getId();
	}
	
	public boolean pending()
	{
		return _expiry < System.currentTimeMillis();
	}
	
	public void cancel()
	{
		SUniverse.getStaticUniverse().removeTimer(this);
		delete();
	}
	
	public void fire()
	{
		SObject o = _object.get();
		if (o == null)
		{
			cancel();
			return;
		}
		
		long delta = o.timerExpiry(this);
		if (delta == S.CANCELTIMER)
		{
			cancel();
			return;
		}
		
		SUniverse.getStaticUniverse().removeTimer(this);
		_expiry += delta;
		SUniverse.getStaticUniverse().addTimer(this);
	}
}
