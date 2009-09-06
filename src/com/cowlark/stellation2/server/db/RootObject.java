/* Superclass of all database objects.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/RootObject.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.io.Serializable;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.S;

public class RootObject implements Identifiable, Serializable
{
    private static final long serialVersionUID = -2026012319820246063L;
    
	@Property(scope = S.GLOBAL)
	private long _id = S.NULL;
	
    public RootObject()
    {
    }
    
    public RootObject initialise(long id)
    {
		_id = id;
		ServerDB.Instance.announceNewObject(this);
		return this;
    }

	public long getId()
    {
        return _id;
    }
	
	public void setId(long id)
    {
	    _id = id;
    }
	
	public void delete()
	{
		ServerDB.Instance.setDeleted(this);
	}
}