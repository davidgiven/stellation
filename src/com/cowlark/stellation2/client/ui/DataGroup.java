/* Represents a group of rows in a DataTable.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/ui/DataGroup.java,v $
 * $Date: 2009/09/19 12:06:09 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.ui;

import java.util.Iterator;
import java.util.Vector;
import com.google.gwt.user.client.ui.Widget;

public class DataGroup implements Iterable<DataRow>
{
	private Vector<DataRow> _rows = new Vector<DataRow>();
	private DataTable _table = null;
	private Widget _header = null;
	private boolean _selectable = false;
	private boolean _hidden = false;
	private boolean _sorted = false;
	private int _absoluteRow;
	private int _selectedRow;
	
	public DataGroup()
    {
    }
	
	public Widget getHeader()
    {
	    return _header;
    }
	
	public DataGroup setHeader(String header)
	{
		_header = new WrappedLabel(header);
		update();
		return this;
	}
	
	public DataGroup setHeader(Widget header)
	{
		_header = header;
		update();
		return this;
	}

	public boolean isSelectable()
    {
	    return _selectable;
    }
	
	public DataGroup setSelectable(boolean selectable)
    {
	    _selectable = selectable;
	    return this;
    }
	
	public boolean isHidden()
    {
	    return _hidden;
    }
	
	public DataGroup setHidden(boolean hidden)
    {
	    _hidden = hidden;
	    return this;
    }
	
	public boolean isSorted()
    {
	    return _sorted;
    }
	
	public DataGroup setSorted(boolean sorted)
    {
	    _sorted = sorted;
	    return this;
    }
	
	public Iterator<DataRow> iterator()
	{
	    return _rows.iterator();
	}
	
	public DataGroup add(DataRow row)
	{
		_rows.add(row);
		update();
		return this;
	}
	
	public DataGroup remove(DataRow row)
	{
		_rows.remove(row);
		update();
		return this;
	}
	
	public Vector<DataRow> getRows()
	{
		return _rows;
	}
	
	public void update()
	{
		if (_table != null)
			_table.update();
	}
	
	int getAbsoluteRow()
    {
	    return _absoluteRow;
    }
	
	void setAbsoluteRow(int row)
	{
		_absoluteRow = row;
	}
	
	protected void onLoad(DataTable table)
	{
		_table = table;
	}
	
	protected void onUnload()
	{
		_table = null;
	}

	public void onClick(int row, int cell)
	{
		if (_selectable)
			_table.selectRow(this, row);
	}
	
	public void onSelectRow(int row)
	{
		_table.addRowStyle(this, row, "SelectedRow");
		_selectedRow = row;
	}
	
	public void onDeselectRow()
	{
		_table.removeRowStyle(this, _selectedRow, "SelectedRow");
		_selectedRow = -1;
	}
}
