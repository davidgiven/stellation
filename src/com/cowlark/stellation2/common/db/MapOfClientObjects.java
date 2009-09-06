/* An ordered map of client-side database objects.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/MapOfClientObjects.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.util.Collection;
import java.util.Map;
import com.cowlark.stellation2.common.model.CObject;

public class MapOfClientObjects extends MapOfObjects<CObject>
{
	public MapOfClientObjects()
    {
    }
	
	public MapOfClientObjects(Map<String, Long> ids)
    {
		super(ids);
    }
	
	public Collection<CObject> values()
    {
		ListOfClientObjects values = new ListOfClientObjects();
		
		for (Long id : getData().values())
			values.add(id);
		
	    return values;
    }
}
