/* An abstract ordered set of objects in the database.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/db/ListOfClientIntegers.java,v $
 * $Date: 2009/09/20 21:46:14 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.db;

import java.util.List;
import java.util.Vector;

public class ListOfClientIntegers extends Vector<Integer>
{
    private static final long serialVersionUID = 1822331373233468905L;
    
    public ListOfClientIntegers()
    {
    }
    
    public ListOfClientIntegers(List<Integer> data)
    {
    	super(data);
    }
}
