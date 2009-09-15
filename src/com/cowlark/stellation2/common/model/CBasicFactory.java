/* Client-side generic ship.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CBasicFactory.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;

public class CBasicFactory extends CFactory
{
	public CBasicFactory()
    {
    }
	
	@Override
	public Properties getProperties()
	{
		return PropertyStore.BasicFactory;
	}
}
