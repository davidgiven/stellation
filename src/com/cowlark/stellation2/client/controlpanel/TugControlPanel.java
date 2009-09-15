/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/TugControlPanel.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CStar;
import com.cowlark.stellation2.common.model.CTug;

public class TugControlPanel extends UnitControlPanel<CTug>
{
	private DataGroup _group;
	private DBRef<CStar> _star = new DBRef<CStar>();
	
	public TugControlPanel(CTug ship)
    {
		super(ship);
    }
	
	@Override
	protected void onLoad()
	{
		registerStarChangeCallback();
	    super.onLoad();
	}
	
	@Override
	protected void onUnload()
	{
	    super.onUnload();
	    unregisterStarChangeCallback();
	}
	
	private void registerStarChangeCallback()
	{
		try
		{
			CTug tug = getObject();
			CStar star = tug.getStar();
			_star = new DBRef<CStar>(star);
			
			ClientDB.addChangeCallback(star, this);
		}
		catch (OutOfScopeException e)
		{
		}		
	}
	
	private void unregisterStarChangeCallback()
	{
		if (_star.isNull())
			return;
		
		CStar star = _star.get();
		ClientDB.removeChangeCallback(star, this);
	}
	
	@Override
	public void onChange(ChangeCallback cb)
	{
	    super.onChange(cb);

	    try
	    {
			CStar star = getObject().getStar();
			if (!_star.equals(star))
			{
				unregisterStarChangeCallback();
				registerStarChangeCallback();
			}			
	    }
	    catch (OutOfScopeException e)
	    {
	    }
	}
}
