/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/Attic/CargoshipResourcesMonitor.java,v $
 * $Date: 2009/09/08 23:01:28 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.monitors.Monitor;
import com.cowlark.stellation2.common.Resources;
import com.cowlark.stellation2.common.Utils;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CCargoship;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public class CargoshipResourcesMonitor extends Monitor<CCargoship>
{
	private DBRef<CStar> _star = DBRef.NULL();
	private VerticalPanel _panel = new VerticalPanel();
	private Grid _grid = new Grid(5, 4);
	private Label _ca = new Label();
	private Label _cm = new Label();
	private Label _co = new Label();
	private TextBox _da = new TextBox();
	private TextBox _dm = new TextBox();
	private TextBox _do = new TextBox();
	private Label _aa = new Label();
	private Label _am = new Label();
	private Label _ao = new Label();
	
	private Button _right = new Button("->",
			new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						copyRight();
					}
				}
			);
	
	private Button _zero = new Button("0",
			new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						zero();
					}
				}
			);

	private Button _left = new Button("<-",
			new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						copyLeft();
					}
				}
			);

	private Button _load = new Button("Load",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
				}
			}
		);

	private Button _unload = new Button("Unload",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
				}
			}
		);
		
	
	private Object[][] _widgets =
	{
			{null,  "Current","Change",  "Available"},
			{"A",   _ca,      _da,      _aa},
			{"M",   _cm,      _dm,      _am},
			{"O",   _co,      _do,      _ao},
			{null,  _right,   _zero,    _left}                      
	};
	
	public CargoshipResourcesMonitor(CCargoship o)
    {
		super(o);
		
		_grid.addStyleName("CargoshipResourcesGrid");
		_right.setWidth("100%");
		_zero.setWidth("100%");
		_left.setWidth("100%");
		Utils.fillGrid(_grid, _widgets);
		
		_load.setWidth("100%");
		_unload.setWidth("100%");
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setWidth("100%");
		hpanel.add(_load);
		hpanel.add(_unload);
		
		CellFormatter f = _grid.getCellFormatter();
		f.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		f.setAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		f.setAlignment(0, 3, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		_grid.setWidth("100%");
		_panel.setWidth("100%");
		_panel.add(_grid);
		_panel.add(hpanel);
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
	
	public String getLabel()
	{
		return "Resources";
	}
	
	private void registerStarChangeCallback()
	{
		try
		{
			CCargoship cargoship = getObject();
			CStar star = cargoship.getParent().getParent().toStar();
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
	
	public Widget updateImpl(CCargoship hr) throws OutOfScopeException
	{
		CStar star = hr.getParent().getParent().toStar();
		if (!_star.equals(star))
		{
			unregisterStarChangeCallback();
			registerStarChangeCallback();
		}
		
		Resources r = hr.getResources();
		_ca.setText(Double.toString(r.getAntimatter()));
		_cm.setText(Double.toString(r.getMetal()));
		_co.setText(Double.toString(r.getOrganics()));
		
		r = star.getResources();
		_aa.setText(Double.toString(r.getAntimatter()));
		_am.setText(Double.toString(r.getMetal()));
		_ao.setText(Double.toString(r.getOrganics()));
		
		return _panel;
	}
	
	private void zero()
	{
		_da.setText("0");
		_dm.setText("0");
		_do.setText("0");
	}
	
	private void copyRight()
	{
		_da.setText(_ca.getText());
		_dm.setText(_cm.getText());
		_do.setText(_co.getText());
	}
	
	private void copyLeft()
	{
		_da.setText(_aa.getText());
		_dm.setText(_am.getText());
		_do.setText(_ao.getText());
	}
}