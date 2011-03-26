package com.cowlark.stellation3.common.controllers;

public interface ObjectSummaryController extends Controller
{
	public void setNameMarkup(String markup);
	public void setDamage(int current, int max);
	public void setDetailsMarkup(String markup);
}
