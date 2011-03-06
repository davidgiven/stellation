package com.cowlark.stellation3.common.controllers;

public interface StarMapStarController extends Controller
{
	static class StarData
	{
		public double x;
		public double y;
		public double brightness;
		public String name;
	}
	
	public void setStarData(StarData sd);
}
