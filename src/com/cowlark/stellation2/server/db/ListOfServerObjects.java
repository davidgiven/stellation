/* An ordered set of server-side database objects.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/db/ListOfServerObjects.java,v $
 * $Date: 2009/09/06 17:59:15 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import com.cowlark.stellation2.common.db.HasDBRepresentation;
import com.cowlark.stellation2.common.db.ListOfClientObjects;
import com.cowlark.stellation2.common.db.ListOfObjects;

public class ListOfServerObjects<T extends RootObject> extends ListOfObjects<T>
		implements HasDBRepresentation, Serializable
{
    private static final long serialVersionUID = -6521571141650721893L;

	public Iterable<String> toDBRepresentation()
	{
		LinkedList<String> list = new LinkedList<String>();
		for (Long id : getData())
			list.add(Long.toString(id));
		return list;
	}

	public void fromDBRepresentation(Iterable<String> db)
	{
		for (String s : db)
		{
			Long id = Long.parseLong(s);
			add(id);
		}
	}

	public Object getClient()
    {
		return new ListOfClientObjects(getData());
    }
}
