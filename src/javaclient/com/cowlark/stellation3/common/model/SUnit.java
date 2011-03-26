package com.cowlark.stellation3.common.model;

import com.cowlark.stellation3.common.monitors.Monitor;
import com.cowlark.stellation3.common.monitors.ObjectSummaryMonitor;

public class SUnit extends SObject
{
	public SUnit(int oid)
	{
		super(oid);
	}
	
	public Monitor<SUnit> createSummaryMonitor()
	{
		return new ObjectSummaryMonitor(this);
	}
	
	public String getSummaryDetailsMarkup()
	{
		return "";
	}
}
