/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/CargoshipControlPanel.java,v $
 * $Date: 2009/09/15 23:15:49 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.view.FullWidthWidgetRow;
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
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

public class CargoshipControlPanel extends UnitControlPanel<CCargoship>
{
	private DataGroup _group;
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
					loadUnload(1);
				}
			}
		);

	private Button _unload = new Button("Unload",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
					loadUnload(-1);
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
	
	
	public CargoshipControlPanel(CCargoship ship)
    {
		super(ship);
		
		_grid.addStyleName("CargoshipResourcesGrid");
		_da.setWidth("100%");
		_do.setWidth("100%");
		_dm.setWidth("100%");
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
		
		_group = addGroup();
		_group.add(new FullWidthWidgetRow(_panel));
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
			CCargoship cargoship = getObject();
			CStar star = cargoship.getStar();
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
			
			Resources r = getObject().getResources();
			_ca.setText(Double.toString(r.getAntimatter()));
			_cm.setText(Double.toString(r.getMetal()));
			_co.setText(Double.toString(r.getOrganics()));
			
			r = star.getResources();
			_aa.setText(Double.toString(r.getAntimatter()));
			_am.setText(Double.toString(r.getMetal()));
			_ao.setText(Double.toString(r.getOrganics()));
	    }
	    catch (OutOfScopeException e)
	    {
	    }
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
	
	private void loadUnload(double delta)
	{
		double a = delta * Double.parseDouble(_da.getText());
		double m = delta * Double.parseDouble(_dm.getText());
		double o = delta * Double.parseDouble(_do.getText());
		
		ClientDB.cargoshipLoadUnload(getId(), a, m, o);
	}
}
