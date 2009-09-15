/* Client-side tug.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/common/model/CTug.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.3 $
 */

package com.cowlark.stellation2.common.model;

import com.cowlark.stellation2.client.controlpanel.TugControlPanel;
import com.cowlark.stellation2.client.view.AbstractView;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.server.db.Property;

public class CTug extends CShip
{
	@Property
	private DBRef<CUnit> _cargo = new DBRef<CUnit>();
	
	public CTug()
    {
    }
	
	@Override
	public Properties getProperties()
	{
	    return PropertyStore.Tug;
	}
	
	public CUnit getCargo() throws OutOfScopeException
    {
		checkOwner();
	    return _cargo.get();
    }
	
	public AbstractView<? extends CObject> createControlPanel()
	{
		return new TugControlPanel(this);
	}
}
