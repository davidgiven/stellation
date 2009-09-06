/* A grouped grid of updatable data.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/DataTable.java,v $
 * $Date: 2009/09/06 17:58:31 $
 * $Author: dtrg $
 * $Revision: 1.1 $
 */

package com.cowlark.stellation2.client.ui;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class DataTable extends Composite implements ClickHandler
{
	private FlexTable _table = new FlexTable();
	private Vector<DataGroup> _groups = new Vector<DataGroup>();
	private TreeMap<Integer, DataGroup> _groupY =
		new TreeMap<Integer, DataGroup>();
	private boolean _loaded = false;
	private boolean _updating = false;
	private DataGroup _selectedGroup = null;
	private int _selectedRowInGroup;
	
	public DataTable(boolean selectable)
    {
		initWidget(_table);
		
		if (selectable)
			_table.addClickHandler(this);
    }
	
	public DataTable()
	{
		this(false);
	}
	
	public Vector<DataGroup> getGroups()
	{
		return _groups;
	}
	
	public DataGroup addGroup()
	{
		DataGroup g = new DataGroup();
		return addGroup(g);
	}
	
	public DataGroup addGroup(DataGroup g)
	{
		_groups.add(g);
		if (_loaded)
			g.onLoad(this);
		update();
		return g;
	}
		
	public DataGroup removeGroup(DataGroup g)
	{
		if (g == _selectedGroup)
		{
			g.onDeselectRow();
			_selectedGroup = null;
		}
		
		_groups.remove(g);
		if (_loaded)
			g.onUnload();
		update();
		return g;
	}
	
	@Override
	protected void onLoad()
	{
	    super.onLoad();
	    for (DataGroup group : _groups)
	    	group.onLoad(this);
	    update();
	    _loaded = true;
	}
	
	@Override
	protected void onUnload()
	{
		_loaded = false;
		_table.clear();
		for (DataGroup group : _groups)
			group.onUnload();
	    super.onUnload();
	}	

	private Command _update_cmd =
		new Command()
		{
			public void execute()
			{
				updateImpl();
			}
		};
		
	public void update()
	{
		if (!_updating)
		{
			DeferredCommand.addCommand(_update_cmd);
			_updating = true;
		}
	}
	
	public void updateImpl()
	{
		_updating = false;
		
		_table.clear();
		_groupY.clear();
		while (_table.getRowCount() > 0)
			_table.removeRow(0);

		int cols = 0;
		for (DataGroup group : _groups)
		{
			for (DataRow row : group)
				cols = Math.max(cols, row.getData().length);
		}

		int y = 0;
		FlexCellFormatter f = _table.getFlexCellFormatter();
		for (DataGroup group : _groups)
		{
			Widget header = group.getHeader();
			if (header != null)
			{
				_table.setWidget(y, 0, header);
				f.setColSpan(y, 0, cols);
				y++;
			}
			
			_groupY.put(y, group);
			group.setAbsoluteRow(y);
			
			for (DataRow row : group)
			{
				int x = 0;
				for (Widget w : row.getData())
				{
					_table.setWidget(y, x, w);
					x++;
				}
				
				f.setColSpan(y, x - 1, cols - x + 1);
				y++;
			}
		}
	}
	
	public void onClick(ClickEvent event)
	{
		Cell cell = _table.getCellForEvent(event);
		int y = cell.getRowIndex();
		
		DataGroup group = null;
		int gy = 0;
		
		for (Map.Entry<Integer, DataGroup> entry : _groupY.entrySet())
		{
			gy = entry.getKey();
			if (gy <= y)
				group = entry.getValue();
			if (gy >= y)
				break;
		}
		
		if (group != null)
			group.onClick(y - gy, cell.getCellIndex());
	}
	
	public void selectRow(DataGroup group, int row)
	{
		if (_selectedGroup != null)
			_selectedGroup.onDeselectRow();
		
		_selectedGroup = group;
		_selectedRowInGroup = row;
		
		_selectedGroup.onSelectRow(row);
	}
	
	public void addRowStyle(DataGroup group, int row, String style)
	{
		row += group.getAbsoluteRow();
		_table.getRowFormatter().addStyleName(row, style);
	}
	
	public void removeRowStyle(DataGroup group, int row, String style)
	{
		row += group.getAbsoluteRow();
		_table.getRowFormatter().removeStyleName(row, style);
	}
}
