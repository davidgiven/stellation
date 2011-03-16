package com.cowlark.stellation3.common.controllers;

import com.cowlark.stellation3.common.model.SObject;

public interface LocationController extends Controller
{
	public void setLocation(SObject location);
	public SObject getLocation();
}
