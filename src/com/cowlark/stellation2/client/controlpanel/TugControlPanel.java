/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/TugControlPanel.java,v $
 * $Date: 2009/09/16 23:14:51 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.monitors.TugCargoMonitor;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.ui.SimpleDataRow;
import com.cowlark.stellation2.client.ui.Trigger;
import com.cowlark.stellation2.client.view.TabularMonitorRow;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.db.ListOfClientObjects;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.common.model.CStar;
import com.cowlark.stellation2.common.model.CTug;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TugControlPanel extends UnitControlPanel<CTug>
{
	private DBRef<CStar> _star = new DBRef<CStar>();
	private SimplePanel _panel = new SimplePanel();
	private ComplexPanel _load = new VerticalPanel();
	private ListBox _loadableList = new ListBox();
	private ListOfClientObjects _loadable = new ListOfClientObjects();

	private Trigger _unload = new Trigger("Unload",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
					unload();
				}	
			}
		);
	
	private Trigger _loadButton = new Trigger("Load",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
					String value = _loadableList.getValue(
							_loadableList.getSelectedIndex());
					load(Long.parseLong(value));
				}	
			}
		);


	public TugControlPanel(CTug ship)
    {
		super(ship);
		
		DataGroup group = addGroup();
		group.add(new TabularMonitorRow(
				new TugCargoMonitor(ship)));
		group.add(new SimpleDataRow(null, _panel));
		
		_panel.setWidth("100%");
		_unload.setWidth("100%");
		_load.setWidth("100%");
		_loadButton.setWidth("100%");
		_loadableList.setSize("100%", "100%");
		
		_load.add(_loadableList);
		_load.add(_loadButton);
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
			
		    CTug tug = getObject();
		    _panel.clear();
		    if (tug.getCargo() != null)
		    	_panel.add(_unload);
		    else
		    {
				/* Update list of loadable objects in this star. */
				
		    	_loadable.clear();
		    	_loadableList.clear();
				for (CObject o : star)
				{
					CUnit unit = o.toUnit();
					if (unit == null)
						continue;
					if (unit.getOwner() == tug.getOwner())
					{
						_loadable.add(unit);
						_loadableList.addItem(unit.getProperties().getName(),
								Long.toString(unit.getId()));
					}
				}
				
				if (_loadable.isEmpty())
				{
					_loadableList.addItem("nothing available");
					_loadableList.setEnabled(false);
					_loadButton.setEnabled(false);
				}
				else
				{
					_loadableList.setEnabled(true);
					_loadButton.setEnabled(true);
				}
				
		    	_panel.add(_load);
		    }
	    }
	    catch (OutOfScopeException e)
	    {
	    }	    
	}
	
	private void load(long id)
	{
		ClientDB.tugLoad(getId(), id);
	}
	
	private void unload()
	{
		ClientDB.tugUnload(getId());
	}
}
