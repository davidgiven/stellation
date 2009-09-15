/* Server-side generc unit.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/server/model/SBasicFactory.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.server.model;

import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.model.CBasicFactory;
import com.cowlark.stellation2.server.db.CClass;

@CClass(name = CBasicFactory.class)
public class SBasicFactory extends SFactory
{
	@Override
	public SBasicFactory initialise()
	{
	    super.initialise();
	    return this;
	}
	
	@Override
	public Properties getProperties()
	{
	    return PropertyStore.BasicFactory;
	}
}
