/* An abstract ordered set of objects in the database.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/ListOfServerIntegers.java,v $
 * $Date: 2009/09/20 21:46:14 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.util.LinkedList;
import com.cowlark.stellation2.common.db.HasDBRepresentation;
import com.cowlark.stellation2.common.db.ListOfClientIntegers;

public class ListOfServerIntegers extends ListOfClientIntegers
		implements HasDBRepresentation
{
    private static final long serialVersionUID = -4478928220686183994L;
    
	public Iterable<String> toDBRepresentation()
	{
		LinkedList<String> list = new LinkedList<String>();
		for (int i : this)
			list.add(Integer.toString(i));
		return list;
	}

	public void fromDBRepresentation(Iterable<String> db)
	{
		for (String s : db)
		{
			Integer i = Integer.parseInt(s);
			add(i);
		}
	}

	public Object getClient()
    {
		return new ListOfClientIntegers(this);
    }
}
