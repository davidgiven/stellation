/* Implements the Administration panel.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/AdminView.java,v $
 * $Date: 2009/09/06 22:17:53 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import java.util.HashMap;
import java.util.Map;
import com.cowlark.stellation2.client.ClientDB;
import com.cowlark.stellation2.client.Stellation2;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class AdminView extends Composite
{
	private FlowPanel _panel = new FlowPanel();
	private TextBox _identry = new TextBox();
	private Button _loadButton = new Button("Load");
	private Button _saveButton = new Button("Save");
	private ScrollPanel _scrollpanel = new ScrollPanel();
	private FlexTable _data = new FlexTable();
	
	public AdminView()
    {
		initWidget(_scrollpanel);
		setStylePrimaryName("SimplePanel");
		
		_scrollpanel.setAlwaysShowScrollBars(false);
		_scrollpanel.add(_panel);
		
		_panel.setStylePrimaryName("PanelInner");
		
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.add(_identry);
		hpanel.add(_loadButton);
		hpanel.add(_saveButton);
		_panel.add(hpanel);
		_panel.add(_data);
		
		_loadButton.addClickHandler(
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						load();
					}
				}
		);
		
		_saveButton.addClickHandler(
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						save();
					}
				}
		);
    }

	private void addRow(String key, String value, int y)
	{
		final Button delete = new Button("X",
				new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						Cell cell = _data.getCellForEvent(event);
						int row = cell.getRowIndex();
						_data.removeRow(row);
					}
				}				
		);
				
		TextBox keyw = new TextBox();
		keyw.setText(key);
		TextBox valuew = new TextBox();
		valuew.setText(value);
		
		_data.setWidget(y, 0, delete);
		_data.setWidget(y, 1, keyw);
		_data.setWidget(y, 2, valuew);
	}
	
	private AsyncCallback<Map<String, String>> _load_cb =
		new AsyncCallback<Map<String, String>>()
	{
		public void onFailure(Throwable caught)
		{
			Stellation2.alert("Failed to load object! " + caught.toString());
		}
		
		public void onSuccess(Map<String, String> result)
		{
			while (_data.getRowCount() > 0)
				_data.removeRow(0);
			
			for (Map.Entry<String, String> entry : result.entrySet())
				addRow(entry.getKey(), entry.getValue(), _data.getRowCount());
			
			Button add = new Button("Add row",
					new ClickHandler()
					{
						public void onClick(ClickEvent event)
						{
							Cell cell = _data.getCellForEvent(event);
							int row = cell.getRowIndex();
							
							_data.insertRow(row);
							addRow("", "", row);
						}
					}
			);
			_data.setWidget(_data.getRowCount(), 0, add);
		}
	};
	
	private void load()
	{
		long id = Long.parseLong(_identry.getText());
		Stellation2.Service.adminLoadObject(Stellation2.getAuthentication(),
				id, _load_cb);
	}
	
	private AsyncCallback<Void> _save_cb =
		new AsyncCallback<Void>()
	{
		public void onFailure(Throwable caught)
		{
			Stellation2.alert("Failed to save object! " + caught.toString());
		}
		
		public void onSuccess(Void result)
		{
			Stellation2.alert("Object saved.");
			ClientDB.fetchUpdates();
		}
	};
	
	private void save()
	{
		Map<String, String> map = new HashMap<String, String>();
		
		for (int y = 0; y < _data.getRowCount(); y++)
		{
			try
			{
				Object keyo = _data.getWidget(y, 1);
				Object valueo = _data.getWidget(y, 2);
		
				if ((keyo != null) && (keyo instanceof TextBox) &&
					(valueo != null) && (valueo instanceof TextBox))
				{
					TextBox keyw = (TextBox) keyo;
					TextBox valuew = (TextBox) valueo;
					
					String key = keyw.getText().trim();
					String value = valuew.getText();
					
					if (!key.equals(""))
						map.put(key, value);
				}
			}
			catch (IndexOutOfBoundsException e)
			{
			}
		}
		
		long id = Long.parseLong(_identry.getText());
		Stellation2.Service.adminSaveObject(Stellation2.getAuthentication(),
				id, map, _save_cb);
	}
}
