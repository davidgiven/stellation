package com.cowlark.stellation3.common.model;

import com.cowlark.stellation3.common.monitors.BrightnessMonitor;
import com.cowlark.stellation3.common.monitors.MonitorGroup;
import com.cowlark.stellation3.common.monitors.PositionMonitor;
import com.cowlark.stellation3.common.monitors.StarFixedUnitsMonitor;

public class SStar extends SObject
{
	public SStar(int oid)
	{
		super(oid);
	}
	
	@Override
	public void constructBasicControlPanelGroup(MonitorGroup group)
	{
	    super.constructBasicControlPanelGroup(group);
	    group.addMonitor(new PositionMonitor(this));
	    group.addMonitor(new BrightnessMonitor(this));
	}
	
	@Override
	public void createControlPanel(MonitorGroup group)
	{
	    super.createControlPanel(group);
	    group.addMonitor(new StarFixedUnitsMonitor(this));
	}
}
