/* Group showing an individual fleet in the middle pane.
 * $Source: /cvsroot/stellation/stellation2/src/com/cowlark/stellation2/client/view/MiddlePaneFleetGroup.java,v $
 * $Date: 2009/09/19 12:06:09 $
 * $Author: dtrg $
 * $Revision: 1.2 $
 */

package com.cowlark.stellation2.client.view;

import java.util.HashSet;
import java.util.Set;
import com.cowlark.stellation2.client.ChangeCallback;
import com.cowlark.stellation2.common.Identifiable;
import com.cowlark.stellation2.common.SyncableObjectList;
import com.cowlark.stellation2.common.SyncableObjectManager;
import com.cowlark.stellation2.common.db.Database;
import com.cowlark.stellation2.common.exceptions.OutOfScopeException;
import com.cowlark.stellation2.common.model.CFleet;
import com.cowlark.stellation2.common.model.CUnit;

public class MiddlePaneFleetGroup extends AbstractGroup<CFleet>
		implements SyncableObjectManager<UnitSummaryRow>
{
    private static final long serialVersionUID = 3599580066086929497L;
    
	private SyncableObjectList<UnitSummaryRow> _shipRows =
		new SyncableObjectList<UnitSummaryRow>();
	
	public MiddlePaneFleetGroup(CFleet fleet)
    {
		super(fleet);
		setSelectable(true);
		setSorted(true);
    }

	@Override
	public void onChange(ChangeCallback cb)
	{
		Set<Identifiable> set = new HashSet<Identifiable>();
		
		try
		{
			CFleet fleet = getObject();
			setHeader(fleet.getName());
			getHeader().setStylePrimaryName("MiddlePaneFleetName");
			set.addAll(fleet.getContents());
		}
		catch (OutOfScopeException e)
		{
		}
	
		_shipRows.syncWith(set, this);
	}	
	
	public UnitSummaryRow create(long id)
	{
		CUnit unit = Database.get(id);
		UnitSummaryRow view = new UnitSummaryRow(unit);
		add(view);
		return view;
	}
	
	public void destroy(UnitSummaryRow object)
    {
		remove(object);
    }

}
