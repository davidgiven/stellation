/* Abstract database superclass.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/Database.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.S;

public abstract class Database
{
	public static Database Instance;
	
	public static <T extends Identifiable> T get(long id)
	{
		if (id == S.NULL)
			return null;
		return Instance.getImpl(id);
	}

	public static <T extends Identifiable> void put(T object)
	{
		Instance.putImpl(object);
	}
	
	public abstract <T extends Identifiable> T getImpl(long id);
	public abstract <T extends Identifiable> void putImpl(T object);
}
