/* Handles the right-hand pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/controlpanel/AbstractControlPanel.java,v $
 * $Date: 2009/09/20 21:49:48 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.controlpanel;

import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.ui.DataGroup;
import com.cowlark.stellation2.client.ui.DataTable;
import com.cowlark.stellation2.client.view.AbstractView;
import com.cowlark.stellation2.client.view.FullWidthWidgetRow;
import com.cowlark.stellation2.common.data.Properties;
import com.cowlark.stellation2.common.db.DBRef;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CStar;
import com.cowlark.stellation2.common.model.CUnit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractControlPanel<T extends CUnit> extends AbstractView<T>
{
	private ScrollPanel _scrollpane = new ScrollPanel();
	private FlowPanel _panel = new FlowPanel();
	private DataTable _data = new DataTable();
	private DBRef<CStar> _star = DBRef.NULL();
	
	public AbstractControlPanel(T object)
    {
		super(object);
		
		initWidget(_scrollpane);
		_scrollpane.setAlwaysShowScrollBars(false);
		_scrollpane.add(_panel);
		
		setStylePrimaryName("SimplePanel");
		_panel.setStylePrimaryName("PanelInner");
		_data.setWidth("100%");
		
		add(_data);

		DataGroup group = addGroup();
		Properties props = object.getProperties();
		HorizontalPanel hpanel = new HorizontalPanel();
		Label label = new Label(props.getName());
		label.addStyleName("UnitBannerTitle");
		hpanel.add(label);
		label = new Label("#"+object.getId());
		label.addStyleName("UnitBannerObjectId");
		hpanel.add(label);
		group.add(new FullWidthWidgetRow(hpanel));
		
		HTML html = new HTML(props.getDescription());
		html.addStyleName("UnitBannerDescription");
		group.add(new FullWidthWidgetRow(html));
    }
	
	public void add(Widget w)
    {
	    _panel.add(w);
    }
	
	public DataGroup addGroup()
	{
		return _data.addGroup();
	}
	
	public CStar getStar()
    {
	    return _star.get();
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
			CUnit object = getObject();
			CStar star = object.getStar();
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
