/* Abstract persistence plugin.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/PersistenceInterface.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.io.IOException;
import java.util.Map;

public abstract class PersistenceInterface
{
	public abstract boolean isCached();
	
	public abstract Map<Long, RootObject> load() throws IOException;
	public abstract void save(Map<Long, RootObject> db) throws IOException;
	public abstract void lock() throws IOException;
	public abstract void unlock() throws IOException;
}
