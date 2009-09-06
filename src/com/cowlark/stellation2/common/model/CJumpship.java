/* Client-side jumpship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CJumpship.java,v $
 * $Date: 2009/09/06 22:15:12 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;

public class CJumpship extends CShip
{
	public CJumpship()
    {
    }

	@Override
	public Properties getProperties()
	{
	    return PropertyStore.Jumpship;
	}
}
