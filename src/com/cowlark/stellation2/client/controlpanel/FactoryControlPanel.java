/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/FactoryControlPanel.java,v $
 * $Date: 2009/09/20 22:14:32 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.controlpanel;

import java.util.Set;
import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.monitors.FactoryBuildingMonitor;
import com.cowlark.stellation2.client.monitors.FactoryCompletionMonitor;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.ui.SimpleDataRow;
import com.cowlark.stellation2.client.ui.Switch;
import com.cowlark.stellation2.client.ui.Trigger;
import com.cowlark.stellation2.client.view.FullWidthWidgetRow;
import com.cowlark.stellation2.client.view.TabularMonitorRow;
import com.cowlark.stellation2.common.Utils;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.data.PropertyStore;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.common.db.ListOfClientIntegers;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CFactory;
import com.cowlark.stellation2.common.model.CFleet;
import com.cowlark.stellation2.common.model.CObject;
import com.cowlark.stellation2.common.model.CStar;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FactoryControlPanel extends UnitControlPanel<CFactory>
		implements ChangeHandler
{
	private DBRef<CStar> _star = new DBRef<CStar>();
	private DataGroup _bothGroup;
	
	private DataGroup _notBuildingGroup;
	private ComplexPanel _notBuilding = new VerticalPanel();
	private ListBox _buildableList = new ListBox();
	private HTML _unitDescription = new HTML();
	private Trigger _buildButton = new Trigger("Build",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
					build();
				}
			}
		);
	private Label _buildTime = new Label();
	private SimplePanel _buildCost = new SimplePanel();
	
	private DataGroup _buildingGroup;
	private Trigger _abortButton = new Trigger("Abort build now",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
					abort();
				}
			}
		);
	private Switch _buildContinuously = new Switch("Keep building these");
	
	private DataGroup _warehouseContentsGroup;

	private DataGroup _warehouseControlsGroup;
	private VerticalPanel _deploy = new VerticalPanel();
	private ListBox _deploytoList = new ListBox();
	private Trigger _deployButton = new Trigger("Deploy",
			new ClickHandler()
			{
				public void onClick(ClickEvent event)
				{
					deploy();
				}
			}
		);
	
	public FactoryControlPanel(CFactory unit)
    {
		super(unit);
	
		_bothGroup = addGroup();
		_bothGroup.add(new TabularMonitorRow(
				new FactoryBuildingMonitor(unit)));
		
		_buildingGroup = addGroup();
		_abortButton.setWidth("100%");
		_buildingGroup.add(new TabularMonitorRow(
				new FactoryCompletionMonitor(unit)));
		_buildingGroup.add(new SimpleDataRow(null, _abortButton));
		//_buildingGroup.add(new FullWidthWidgetRow(_buildContinuously));
		
		_notBuildingGroup = addGroup();
		_notBuildingGroup.add(new SimpleDataRow(null, _notBuilding));
		_unitDescription.addStyleName("UnitBannerDescription");
		_notBuildingGroup.add(new FullWidthWidgetRow(_unitDescription));
		_notBuildingGroup.add(new SimpleDataRow(null, new Label("Build time:"), _buildTime));
		_notBuildingGroup.add(new SimpleDataRow(null, new Label("Build cost:"), _buildCost));
		
		_notBuilding.add(_buildableList);
		_notBuilding.add(_buildButton);
		_notBuilding.setWidth("100%");
		_buildableList.setWidth("100%");
		_buildButton.setWidth("100%");
		
		Set<Integer> buildable = unit.getProperties().getBuildable();
		for (int i : buildable)
		{
			Properties properties = PropertyStore.getProperties(i);
			_buildableList.addItem(properties.getName(), Integer.toString(i));
		}
		
		_buildableList.addChangeHandler(this);		
		onChange((ChangeEvent) null);
		
		_warehouseContentsGroup = addGroup();
		_warehouseContentsGroup.setHeader("Currently warehoused:");
		_warehouseContentsGroup.getHeader().addStyleName("RightPaneGroupHeader");
		_warehouseControlsGroup = addGroup();
		_deploy.setWidth("100%");
		_deploytoList.setWidth("100%");
		_deployButton.setWidth("100%");
		_deploy.add(_deploytoList);
		_deploy.add(_deployButton);
		_warehouseControlsGroup.add(_deploy);
    }
	
	private int getSelectedBuildableType()
	{
		return Integer.parseInt(
				_buildableList.getValue(_buildableList.getSelectedIndex()));
	}
	
	private CFleet getSelectedFleet()
	{
		long id = Long.parseLong(
				_deploytoList.getValue(_deploytoList.getSelectedIndex()));
		return Database.get(id);
	}
	
	public void onChange(ChangeEvent event)
	{
		int buildable = getSelectedBuildableType();
		Properties properties = PropertyStore.getProperties(buildable);
		_unitDescription.setHTML(properties.getDescription());
		_buildTime.setText(Utils.renderDuration(properties.getBuildTime()));
		
		_buildCost.clear();
		_buildCost.add(Utils.renderResources(properties.getBuildCost()));
	}
	
	@Override
	public void onChange(ChangeCallback cb)
	{
	    super.onChange(cb);

	    try
	    {
	    	CFactory factory = getObject();
	    	
	    	/* Which control sets are being shown? */
	    	
	    	if (factory.getNowBuilding() == PropertyStore.NOTHING)
	    	{
	    		_buildingGroup.setHidden(true);
	    		_notBuildingGroup.setHidden(false);
	    	}
	    	else
	    	{
	    		_buildingGroup.setHidden(false);
	    		_notBuildingGroup.setHidden(true);
	    	}

	    	/* Update the deployer. */
	    	
	    	ListOfClientIntegers warehouse = factory.getWarehouse();
	    	if (!warehouse.isEmpty())
	    	{
	    		_warehouseContentsGroup.setHidden(false);
	    		_warehouseControlsGroup.setHidden(false);
	    		
	    		/* Update the warehouse contents. */
	    		
	    		_warehouseContentsGroup.clear();
	    		for (int type : warehouse)
	    		{
	    			_warehouseContentsGroup.add(
	    					new SimpleDataRow(null,
	    							new Label(
	    									PropertyStore.getProperties(type).getName())));
	    		}
	    		
		    	/* Update the deployer's list of available fleets. */
		    	
		    	_deploytoList.clear();
		    	for (CObject o : getStar())
		    	{
		    		CFleet fleet = o.toFleet();
		    		if ((fleet != null) && (fleet.getOwner() == factory.getOwner()))
		    		{
		    			_deploytoList.addItem(fleet.getName(),
		    					Long.toString(fleet.getId()));
		    		}
		    	}
	    	}
	    	else
	    	{
	    		_warehouseContentsGroup.setHidden(true);
	    		_warehouseControlsGroup.setHidden(true);
	    	}
	    }
	    catch (OutOfScopeException e)
	    {
	    	_buildingGroup.setHidden(true);
	    	_notBuildingGroup.setHidden(true);
	    	_warehouseContentsGroup.setHidden(true);
	    	_warehouseControlsGroup.setHidden(true);
	    }
	}
	
	private void build()
	{
		ClientDB.factoryBuild(getId(), getSelectedBuildableType());
	}
	
	private void abort()
	{
		ClientDB.factoryAbort(getId());
	}
	
	private void deploy()
	{
		ClientDB.factoryDeploy(getId(), getSelectedFleet()); 
	}
}
