/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/UnitControlPanel.java,v $
 * $Date: 2009/09/14 22:22:32 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.common.Utils;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class UnitControlPanel<T extends CUnit> extends AbstractControlPanel<T>
{
	private Label _mass = new Label();
	private SimplePanel _upkeep = new SimplePanel();
	
	private Object[][] _rows = new Object[][]
    {
		{"Mass:",            _mass},
		{"Upkeep /hr:",      _upkeep}
    };
    
	public UnitControlPanel(T object)
	{
		super(object);
		
		DataGroup group = addGroup();
		Utils.fillGroup(group, _rows);
	}
	
	@Override
	public void onChange(ChangeCallback cb)
	{
	    super.onChange(cb);
	    
	    try
	    {
	    	CUnit unit = getObject();
	 
	    	_mass.setText(Utils.renderMass(unit.getMass()));
	    	_upkeep.clear();
	    	_upkeep.add(Utils.renderResources(unit.getProperties().getMaintenanceCost()));
	    }
	    catch (OutOfScopeException e)
	    {
	    }
	}
}
