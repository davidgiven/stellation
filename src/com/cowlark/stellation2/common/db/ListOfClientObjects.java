/* An ordered set of client-side database objects.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/ListOfClientObjects.java,v $
 * $Date: 2009/09/06 17:59:16 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.util.Collection;
import com.cowlark.stellation2.common.model.CObject;

public class ListOfClientObjects extends ListOfObjects<CObject>
{
	public ListOfClientObjects()
    {
		super();
    }
	
	public ListOfClientObjects(Collection<Long> ids)
    {
		super(ids);
    }
}
