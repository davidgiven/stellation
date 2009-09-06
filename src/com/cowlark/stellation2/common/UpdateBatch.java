/* A change delta from the server.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/UpdateBatch.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.cowlark.stellation2.common.db.LogMessageStore;
import com.cowlark.stellation2.common.model.CObject;
import com.google.gwt.user.client.rpc.IsSerializable;

public class UpdateBatch implements Iterable<CObject>, IsSerializable
{
	private long _time;
	private Set<CObject> _updates = new HashSet<CObject>();
	private Set<Long> _visible = new HashSet<Long>();
	private LogMessageStore _logUpdates;
	
	public long getTime()
    {
	    return _time;
    }
	
	public void setTime(long time)
    {
	    _time = time;
    }
	
	public Set<CObject> getUpdatedObjects()
    {
	    return _updates;
    }

	public Set<Long> getVisibleObjects()
	{
		return _visible;
	}
	
	public void setVisibleObjects(Set<Long> visible)
	{
		_visible = visible;
	}

	public LogMessageStore getLogUpdates()
    {
	    return _logUpdates;
    }
	
	public void setLogUpdates(LogMessageStore logUpdates)
    {
	    _logUpdates = logUpdates;
    }
	
	public void add(CObject object)
	{
		_updates.add(object);
	}
	
	public Iterator<CObject> iterator()
	{
	    return _updates.iterator();
	}
}
